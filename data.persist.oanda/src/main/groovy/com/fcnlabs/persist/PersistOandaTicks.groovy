package com.fcnlabs.persist

import com.datastax.driver.core.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

import com.datastax.driver.core.exceptions.*
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer

import org.json.simple.JSONObject
import org.json.simple.JSONValue

import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.LogManager

class PersistOandaTicks {

    private static final Logger log = LogManager.getLogger(PersistOandaTicks.class)

    public static void main(String[] args) {
        // Get configs from environment variables and
        // declare variables
        def env = System.getenv()
        String cassandra_host = env['CQLSH_HOST']
        String kafka_host = env['KAFKA_HOST']
        ZonedDateTime zonedDateTime = null
        LocalDateTime localDateTime = null
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd' 'HHmmssSSS")
        DateTimeFormatter cassie_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'000Z'")
        String fcnDateTime = null

        // Configure Cassandra Connection
        final CassandraConnector client = new CassandraConnector()
        final String ipAddress = cassandra_host
        final int port = args.length > 1 ? Integer.parseInt(args[1]) : 9042
        log.info("Connecting to IP Address " + ipAddress + ":" + port + "...")

        // Connect To Cassandra
        try {
            client.connect(ipAddress, port)
        } catch (Exception e) {
            log.error("Error connecting to Cassandra." + "\n\n" + e)
            return
        }

        log.debug("Setting Kafka Connection properties")
        // Configure Kafka Connection
        Properties props = new Properties()
        props.put("bootstrap.servers", kafka_host + ":9092")
        props.put("group.id", "persist.oanda.ticks")
        props.put("client.id", "20")
        props.put("enable.auto.commit", "true")
        props.put("auto.commit.interval.ms", "1000")
        props.put("session.timeout.ms", "30000")
        props.put("max.poll.records", "10")
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")

        log.debug("About to create KafkaConsumer")
        // Connect To Kafka
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)

        log.debug("About to consumer.subscribe to oandaticks")
        // Subscribe To 'oandaticks' topic
        consumer.subscribe(Arrays.asList("oandaticks"))

        // Loop until we exit via Exception
        Integer tickCounter = 1
        while (true) {
            // Get records from the Kafka topic 'ticks'
            log.trace("about to poll")
            ConsumerRecords<String, String> records = consumer.poll(100)
            for (ConsumerRecord<String, String> record : records) {
                log.debug("offset = " + record.offset() + " key = " + record.key() + " value = " + record.value())

                // record.value() = {"tick":{"instrument":"EUR_USD","time":"2016-08-01T11:54:01.613449Z","bid":1.11593,"ask":1.11607}}
                //
                // Row Fields:
                // DateTime Stamp,Bid Quote,Ask Quote,Volume
                //
                // DateTime Stamp Format:
                // ...

                Object obj = JSONValue.parse(record.value())
                JSONObject tick = (JSONObject) obj

                // unwrap if necessary
                if (tick.containsKey("tick")) {
                    tick = (JSONObject) tick.get("tick")
                }

                String data_row = null

                // ignore heartbeats
                if (tick.containsKey("instrument")) {

                    String instrument = tick.get("instrument").toString()
                    String time = tick.get("time").toString()
                    Float bid = Float.parseFloat(tick.get("bid").toString())
                    Float ask = Float.parseFloat(tick.get("ask").toString())

                    log.debug("Inst: " + instrument + " Time: " + time + " Big: " + bid + " Ask: " + ask)

                    LocalDate day = LocalDate.fromYearMonthDay(time.substring(0,4).toInteger(),
                                                               time.substring(5,7).toInteger(),
                                                               time.substring(8,10).toInteger())


                    Long nanos = 0
                    nanos += time.substring(11,13).toLong() * 1000000000 * 60 * 60
                    nanos += time.substring(14,16).toLong() * 1000000000 * 60
                    nanos += time.substring(17,19).toLong() * 1000000000
                    nanos += time.substring(20,26).toLong()

                    //CREATE TABLE oanda_ticks.eurusd_ticks (
                    //   day date,
                    //   nanos time,
                    //   ask float,
                    //   bid float,
                    //   PRIMARY KEY (day, nanos)
                    //) WITH CLUSTERING ORDER BY (nanos ASC)
                    //   AND bloom_filter_fp_chance = 0.1
                    //   AND caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
                    //   AND comment = ''
                    //   AND compaction = {'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'}
                    //   AND compression = {'chunk_length_in_kb': '64', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}
                    //   AND crc_check_chance = 1.0
                    //   AND dclocal_read_repair_chance = 0.1
                    //   AND default_time_to_live = 0
                    //   AND gc_grace_seconds = 864000
                    //   AND max_index_interval = 2048
                    //   AND memtable_flush_period_in_ms = 0
                    //   AND min_index_interval = 128
                    //   AND read_repair_chance = 0.0

                    try {
                        client.getSession().execute(
                            "INSERT INTO oanda_ticks.eurusd_ticks (day, nanos, bid, ask) VALUES (?, ?, ?, ?)",
                                day, nanos, bid, ask)

                        tickCounter += 1
                        //log.info(".")
                        if (tickCounter > 1000) {
                            log.info("1000 ticks persisted.  Resetting...")
                            tickCounter = 1
                        }
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
                        return
                    } catch (QueryValidationException e) {
                        log.error("Error inserting tick to Cassandra.  QueryValidation." + "\n\n" + e)
                        client.close()
                        return
                    }
                }
            }
        }
        // after while loop...theoretical
        client.close()
        consumer.close()
    }
}
