package com.fcnlabs.oanda

import groovyx.net.http.HTTPBuilder

// Get tickstream data from OANDA's streaming tick api and immediately
// publish it to Kafka queue eurusd_ticks

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord

//import kafka.producer.KeyedMessage
//import kafka.producer.ProducerConfig
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.message.BasicHeader
import org.apache.http.util.EntityUtils

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.LogManager

class PublishTickStream {

    private static final Logger log = LogManager.getLogger(PublishTickStream.class)

    public static void main (String[]args) {
        def env                     = System.getenv()
        String api_key              = env['OANDA_API_KEY']
        String api_id               = env['OANDA_API_ID']
        String kafka_host           = env['KAFKA_HOST']
        String zk_host              = env['ZK_HOST']
        def authMap                 = [:]
        ZonedDateTime zonedDateTime = null
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:MM:ss.SSSZ")
        String isoDateTime          = null
        Long ts                     = null

        log.info("Connecting to Oanda Tick Streaming API...")
        authMap['Authorization'] = "Bearer " + api_key

        def httpBuilder = new HTTPBuilder('https://stream-fxtrade.oanda.com')

        HttpClient httpClient = HttpClientBuilder.create().build()


        final String ipAddress = kafka_host
        final int port = args.length > 1 ? Integer.parseInt(args[1]) : 9092
        log.info("Connecting to Kafka Broker at IP Address " + ipAddress + ":" + port + "...")

        Properties props = new Properties()
        props.put("bootstrap.servers", kafka_host + ":" + port.toString())
        props.put("acks", "all")
        props.put("retries", 0)
        props.put("batch.size", 16384)
        props.put("linger.ms", 1)
        props.put("buffer.memory", 33554432)
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

        Producer<String, String> producer = new KafkaProducer<>(props)

        HttpEntity entity = null
        Integer processedRecords = 0
        Integer tickler = 1000
        Date startTime = new Date()
        log.info("Start time: " + new Date().toString())

        try {

            String domain 	= "https://stream-fxtrade.oanda.com"
            String accountId 	= api_id
            String instruments 	= "EUR_USD"

            HttpUriRequest httpGet = new HttpGet(domain + "/v1/prices?accountId=" + accountId + "&instruments=" + instruments)
            httpGet.setHeader(new BasicHeader("Authorization", "Bearer " + api_key))

            log.debug("Executing request: " + httpGet.getRequestLine())

            HttpResponse resp = httpClient.execute(httpGet)
            entity = resp.getEntity()

            if (resp.getStatusLine().getStatusCode() == 200 && entity != null) {

                InputStream stream = entity.getContent()
                String line
                BufferedReader br = new BufferedReader(new InputStreamReader(stream))

                while ((line = br.readLine()) != null) {

                    // Create message to be sent to "tick_topic" topic with the tick
                    ProducerRecord<String, String> data = new ProducerRecord<String, String>("oandaticks", line)

                    // Send the message
                    producer.send(data)

                    processedRecords++

                    if (processedRecords > tickler) {
                        log.info(tickler.toString() + " records processed.  New total: " + processedRecords.toString())
                        tickler = tickler + tickler
                        Date intervalTime = new Date()
                        // interval between two times in milliseconds
                        Long interimInterval = intervalTime.getTime() - startTime.getTime()
                        log.info("Transactions Per Minute: " + (processedRecords / (interimInterval * 60000)).toString())
                    }
                }
            } else {
                // print error message
                String responseString = EntityUtils.toString(entity, "UTF-8")
                log.error(responseString)
            }
        } catch (Exception e) {
            log.error("Error processing OANDA ticks." + "\n\n" + e)
            httpClient.getConnectionManager().shutdown()
            producer.close()
            return
        } finally {
            httpClient.getConnectionManager().shutdown()
            producer.close()
        }
    }
}
