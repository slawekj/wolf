package com.fcnlabs.hist

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.JavaFileNotFoundException

class PublishHistTicks {

    private static final Logger log = LogManager.getLogger(PublishHistTicks.class)

    public static void main(String[] args) {
        def env                 = System.getenv()
        String kafka_host       = env['KAFKA_HOST']
        String histdata_dir     = env['HISTDATA_DIR']

        final String ipAddress = kafka_host
        final int port = args.length > 1 ? Integer.parseInt(args[1]) : 9092
        log.info("Connecting to Kafka Broker at IP Address " + ipAddress + ":" + port + "...")

        Properties props = new Properties();
        props.put("bootstrap.servers", kafka_host + ":" + port.toString())
        props.put("acks", "all")
        props.put("retries", 0)
        props.put("batch.size", 16384)
        props.put("linger.ms", 1)
        props.put("buffer.memory", 33554432)
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

        Producer<String, String> producer = new KafkaProducer<>(props)

        int i, j
        String jj
        String maxts = "2014-02-05T10:53:03.147000Z"

        for (i = 2014; i < 2017; i++) {
            for (j = 1; j < 13; j++) {
                jj = j.toString()
                if (j < 10) { jj = "0" + jj }

                Integer totalRecords = 0

                String filename = histdata_dir + '/DAT_ASCII_EURUSD_T_' + i.toString() + jj + '.csv'

                log.info("Processing " + filename)

                try {
                    File file = new File ( filename )
                } catch (JavaFileNotFoundException e) {
                    log.info("File " + filename + " not found.")
                    break
                }

                file.eachLine { line ->
                    totalRecords++
                }
                log.debug("Total records in csv: " + totalRecords.toString())

                File fileSecondPass = new File ( filename )

                Integer processedRecords = 0
                Double tickler = 0.10
                Date startTime = new Date()
                log.info("Start time: " + new Date().toString())
                fileSecondPass.eachLine { line ->

                    //log.debug("Before new ProducerRecord")

                    // Create message to be sent to "tick_topic" topic with the tick
                    ProducerRecord<String, String> data = new ProducerRecord<String, String>("histticks", line)

                    //log.debug("Before producer.send")

                    // Send the message
                    producer.send(data)

                    processedRecords++

                    if ((processedRecords / totalRecords) > tickler) {
                        log.info((tickler * 100).toString() + "% records processed.  " + processedRecords.toString() + " / " + totalRecords.toString())
                        tickler = tickler + 0.10
                        Date intervalTime = new Date()
                        // interval between two times in milliseconds
                        Long interimInterval = intervalTime.getTime() - startTime.getTime()
                        log.info(""Transactions Per Minute: " + (processedRecords / (interimInterval * 60000)).toString())
                    }
                }
                Date endTime = new Date()
                // interval between two times in milliseconds
                Long interval = endTime.getTime() - startTime.getTime()
                log.info("Stop time: " + new Date().toString() + " Transactions Per Minute: " + (processedRecords / (interval * 60000)).toString())
            }
        }
        producer.close()
    }
}
