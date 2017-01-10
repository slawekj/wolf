package com.fcnlabs.persist

import com.datastax.driver.core.LocalDate
import com.datastax.driver.core.exceptions.*
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class PersistHistTicks {

    private static final Logger log = LogManager.getLogger(PersistHistTicks.class)

    public static void main(String[] args) {
        // Get configs from environment variables and
        // declare variables
        def env = System.getenv()
        String cassandra_host = env['CQLSH_HOST']
        String kafka_host = env['KAFKA_HOST']

        // Configure Cassandra Connection
        final CassandraConnector client = new CassandraConnector()
        final String ipAddress = cassandra_host;
        final int cassandra_port = 9042;
        final int kafka_port = 9092;
        log.info("Connecting to IP Address " + ipAddress + ":" + cassandra_port + "...");

        // Connect To Cassandra
        try {
            client.connect(ipAddress, cassandra_port);
        } catch (Exception e) {
            log.error("Error connecting to Cassandra." + "\n\n" + e)
            return;
        }

        log.debug("Setting Kafka Connection properties")
        // Configure Kafka Connection
        Properties props = new Properties()
        props.put("bootstrap.servers", kafka_host + ":" + kafka_port.toString())
        props.put("group.id", "persist.hist.ticks")
        props.put("client.id", "10")
        props.put("enable.auto.commit", "true")
        props.put("auto.commit.interval.ms", "1000")
        props.put("session.timeout.ms", "30000")
        props.put("max.poll.records", "10")
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")

        log.debug("About to create KafkaConsumer")
        // Connect To Kafka
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)

        log.debug("About to consumer.subscribe to histticks")
        // Subscribe To 'histticks' topic
        consumer.subscribe(Arrays.asList("histticks"))

        // Loop until we exit via Exception
        Date startTime = new Date()
        Integer processedRecords = 0
        Double tickler = 1000
        ////log.debug("About to do while(true)")
        while (true) {
            ////log.debug("Inside while loop.  About to poll.")
            // Get records from the Kafka topic 'ticks'
            ConsumerRecords<String, String> records = consumer.poll(100)
            ////log.debug("Back from poll.  records.size() is: " + records.size().toString())
            for (ConsumerRecord<String, String> record : records) {
                ////log.debug("offset = " + record.offset() + " key = " + record.key() + " value = " + record.value())

                // record.value() example:   20120201 000003660,1.306600,1.306770,0
                //
                //
                // Row Fields:
                // DateTime Stamp,Bid Quote,Ask Quote,Volume
                //
                // DateTime Stamp Format:
                // YYYYMMDD HHMMSSNNN
                //
                // Legend:
                // YYYY – Year
                // MM – Month (01 to 12)
                // DD – Day of the Month
                // HH – Hour of the day (in 24h format)
                // MM – Minute
                // SS – Second
                // NNN – Millisecond

                def recValues = record.value().tokenize(',')

                def (rawDay, rawNanos) = recValues[0].tokenize(' ')

                ////log.debug("About to attempt LocalDate.")
                int tempY = rawDay.substring(0,4).toInteger()
                int tempM = rawDay.substring(4,6).toInteger()
                int tempD = rawDay.substring(6,8).toInteger()

                LocalDate day = LocalDate.fromYearMonthDay(rawDay.substring(0,4).toInteger(),
                                                                  rawDay.substring(4,6).toInteger(),
                                                                  rawDay.substring(6,8).toInteger())

                //Date dateNanos = new Date().parse('YYYYMMDD HHMMSSNNN', recValues[0], TimeZone.getTimeZone("EST"))

                //LocalDate day2 = new LocalDate().fromYearMonthDay(dateNanos.getYear(),
                //                                                  dateNanos.getMonth(),
                //                                                  dateNanos.getDay())

                ////log.debug("About to calc nanos.")
                Long nanos = 0
                nanos += rawNanos.substring(0,2).toLong() * 1000000000 * 60 *60
                nanos += rawNanos.substring(2,4).toLong() * 1000000000 * 60
                nanos += rawNanos.substring(4,6).toLong() * 1000000000
                nanos += rawNanos.substring(6,9).toLong() * 1000000

                Float bid = Float.parseFloat(recValues[1])
                Float ask = Float.parseFloat(recValues[2])
                //Integer volume = Integer.parseInt(recValues[3])

                //System.out.println(fcnDateTime);
                //System.out.println(bid);
                //System.out.println(ask);
                //CREATE TABLE IF NOT EXISTS eurusd_ticks (
                //        day   date,
                //        nanos time,
                //        bid   float,
                //        ask   float,
                //        PRIMARY KEY (day, nanos)
                //) WITH compaction = {'class' : 'LeveledCompactionStrategy' };

                //CREATE TABLE IF NOT EXISTS eurusd_ticks_tps (
                //        day  date,
                //        hour int,
                //        min  int,
                //        sec  int,
                //        tps  int,
                //        PRIMARY KEY (day, hour, min, sec)
                //) WITH compaction = {'class' : 'LeveledCompactionStrategy' };

                try {
                    ////log.debug("About to INSERT day " + day.toString() + " nanos " + nanos.toString() + " bid " + bid.toString() + " ask " + ask.toString())
                    client.getSession().execute(
                        "INSERT INTO hist_ticks.eurusd_ticks (day, nanos, bid, ask) VALUES (?, ?, ?, ?)",
                            day, nanos, bid, ask)

                } catch (NoHostAvailableException e) {
                    log.error("Error inserting tick to Cassandra.  NoHostAvailable." + "\n\n" + e)
                    client.close()
                    return
                } catch (UnavailableException e) {
                    log.error("Error inserting tick to Cassandra.  Unavailable." + "\n\n" + e)
                    client.close()
                    return
                } catch (ReadTimeoutException e) {
                    log.error("Error inserting tick to Cassandra.  ReadTimeout." + "\n\n" + e)
                    client.close()
                    return
                } catch (WriteTimeoutException e) {
                    log.error("Error inserting tick to Cassandra.  WriteTimeout." + "\n\n" + e)
                    client.close()
                    return;
                } catch (QueryValidationException e) {
                    log.error("Error inserting tick to Cassandra.  QueryValidation." + "\n\n" + e)
                    client.close()
                    return
                }

                processedRecords++

                if (processedRecords > tickler) {
                    Date intervalTime = new Date()

                    // interval between two times in milliseconds
                    Long interimInterval = intervalTime.getTime() - startTime.getTime()
                    Integer tps = processedRecords / (interimInterval * 1000)
                    log.info("Processed " + tickler + " records at tps of " + tps.toString())
                    tickler = tickler + tickler
                }
            }
        }
        // theoretical
        client.close()
    }
}
