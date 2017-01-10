package com.fcnlabs.persist

import com.datastax.driver.core.exceptions.*
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.json.simple.JSONObject
import org.json.simple.JSONValue

import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.LogManager

class PersistTickStream {

    private static final Logger log = LogManager.getLogger(PersistTickStream.class)

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

        // Configure Kafka Connection
        Properties props = new Properties()
        props.put("bootstrap.servers", kafka_host + ":9092")
        props.put("group.id", "persist.tick.stream")
        props.put("enable.auto.commit", "true")
        props.put("auto.commit.interval.ms", "1000")
        props.put("session.timeout.ms", "30000")
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")

        // Connect To Kafka
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)

        // Subscribe To 'ticks' topic
        consumer.subscribe(Arrays.asList("ticks"))

        // Loop until we exit via Exception
        while (true) {
            // Get records from the Kafka topic 'ticks'
            ConsumerRecords<String, String> records = consumer.poll(100)
            for (ConsumerRecord<String, String> record : records) {
                log.debug("offset = " + record.offset() + " key = " + record.key() + " value = " + record.value())

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
                    double bid = Double.parseDouble(tick.get("bid").toString())
                    double ask = Double.parseDouble(tick.get("ask").toString())

                    log.debug("Inst: " + instrument + " Time: " + time + " Big: " + bid + " Ask: " + ask)

                    try {
                        client.getSession().execute(
                            "INSERT INTO tick_keyspace.eurusd_ticks (ts, bid, ask) VALUES (?, ?, ?)",
                                time, bid, ask)
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
    }
}
