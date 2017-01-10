package com.fcnlabs.persist
      
import com.datastax.driver.core.Cluster
import com.datastax.driver.core.Host
import com.datastax.driver.core.Metadata
import com.datastax.driver.core.Session
      
import static java.lang.System.out
      
/** 
 * Class used for connecting to Cassandra database. 
 */  
public class CassandraConnector  
{  
       /** Cassandra Cluster. */  
       private Cluster cluster;  
      
       /** Cassandra Session. */  
       private Session session;  
      
       /** 
        * Connect to Cassandra Cluster specified by provided node IP 
        * address and port number. 
        * 
        * @param node Cluster node IP address. 
        * @param port Port of cluster host. 
        */  
       public void connect(final String node, final int port)  
       {  
          this.cluster = Cluster.builder().addContactPoint(node).withPort(port).withCredentials('cassandra', 'cassandra').build();
          final Metadata metadata = cluster.getMetadata();  
          out.printf("Connected to cluster: %s\n", metadata.getClusterName());  
          for (final Host host : metadata.getAllHosts())  
          {  
             out.printf("Datacenter: %s; Host: %s; Rack: %s\n",  
                host.getDatacenter(), host.getAddress(), host.getRack());  
          }  
          session = cluster.connect();  
       }  
      
       /** 
        * Provide my Session. 
        * 
        * @return My session. 
        */  
       public Session getSession()  
       {  
          return this.session;  
       }  
      
       /** Close cluster. */  
       public void close()  
       {  
          cluster.close();  
       }  
}  
