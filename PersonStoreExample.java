package Ignite.mysql;

import java.util.Collections;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.lang.IgniteRunnable;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;



public class PersonStoreExample {
	private static Connection connect = null;
    private static Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private static ResultSet rs = null;
    public static void main(String[] args) throws Exception {
    	 
    	rs = readDataBase();
    //	writeResultSet(rs);
    	
    	IgniteConfiguration cfg = new IgniteConfiguration();

         // The node will be started as a client node.
         cfg.setClientMode(true);

         // Classes of custom Java logic will be transferred over the wire from this app.
         cfg.setPeerClassLoadingEnabled(true);

         // Setting up an IP Finder to ensure the client can locate the servers.
         TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
         ipFinder.setAddresses(Collections.singletonList("127.0.0.1:47500..47509"));
         cfg.setDiscoverySpi(new TcpDiscoverySpi().setIpFinder(ipFinder));

         // Starting the node
        Ignition.setClientMode(true);
        try (Ignite ignite = Ignition.start(cfg)) {
            try (IgniteCache<Long, Person> cache = ignite.getOrCreateCache("personCache")) {
                // Load cache with data from the database.
            	cache.clear();
                cache.loadCache(null);
                // Execute query on cache.
                AutoCloseable cursor = cache.query(new SqlFieldsQuery(
                        "select * from Person "));
                System.out.println(((QueryCursor<java.util.List<?>>) cursor).getAll());
              
             //   ignite.compute(ignite.cluster().forServers()).broadcast(new RemoteTask());
            }
        }
    }
    
    public static ResultSet readDataBase() throws Exception {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost/person?"
                            + "user=root&password=123Sgksk*");
            statement = connect.createStatement();
            System.out.println("connected to DB");
            ResultSet rs = statement.executeQuery("select * from person");
           // writeResultSet(resultSet);
            return rs;
        }
        catch (Exception e) {
            throw e;
        }
    }
    public static void writeResultSet(ResultSet rs) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
	    int columnsNumber = rsmd.getColumnCount();
	    while (rs.next()) {
	        for (int i = 1; i <= columnsNumber; i++) {
	            if (i > 1) System.out.print(" | ");
	            System.out.print(rs.getString(i));
	        }
	        System.out.println("");
	}

}
private static class RemoteTask implements IgniteRunnable {
       
		
		@IgniteInstanceResource
        Ignite ignite;
        @Override public void run() 
        
        {
            System.out.println(">> Executing the compute task in remote servers ");
            IgniteCache<Long, Person> cache = ignite.getOrCreateCache("personCache");
            AutoCloseable cursor = cache.query(new SqlFieldsQuery(
                    "select * from person"));
            System.out.println(((QueryCursor<java.util.List<?>>) cursor).getAll());
    }
    }
}