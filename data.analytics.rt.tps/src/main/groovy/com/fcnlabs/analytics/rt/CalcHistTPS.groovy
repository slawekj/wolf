package com.fcnlabs.analytics.rt

import com.datastax.driver.core.LocalDate
import com.datastax.driver.core.exceptions.*
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class CalcHistTPS {

    private static final Logger log = LogManager.getLogger(CalcHistTPS.class)

    public static void main(String[] args) {
        // Get configs from environment variables and
        // declare variables
        def env = System.getenv()
        String cassandra_host = env['CQLSH_HOST']

        // Configure Cassandra Connection
        final CassandraConnector client = new CassandraConnector()
        final String ipAddress = cassandra_host;
        final int cassandra_port = 9042;
        log.info("Connecting to IP Address " + ipAddress + ":" + cassandra_port + "...");

        // Connect To Cassandra
        try {
            client.connect(ipAddress, cassandra_port);
        } catch (Exception e) {
            log.error("Error connecting to Cassandra." + "\n\n" + e)
            return;
        }

        // Loop until we exit via Exception
        Date    startTime        = new Date()
        Integer processedRecords = 0
        Double  tickler          = 1000

        Integer year  = 2014;
        Integer month = 1;
        Integer day   = 1;

        while (year <= startTime.year()) {

            while (month < 13) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                Date convertedDate = dateFormat.parse(date);
                Calendar c = Calendar.getInstance();
                c.setTime(convertedDate);
                c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));

                while (day <= 

                try {

                    String query =

                                   "SELECT * FROM hist_ticks.eurusd_ticks " +
                                   " WHERE day=:day " +
                                   "    BY nanos ASC;"

                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("day", year.toString + "-" + month.toString() + "-" + day.toString());

                    SimpleStatement statement = new SimpleStatement(query, params);

                    ResultSet results = client.getSession().execute(statement);

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
