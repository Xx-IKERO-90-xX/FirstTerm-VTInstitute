package org.vtinstitute.connection;
import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    // Function to open a connection to the PostgreSQL database
    public Connection openConnection() {
        Connection conn = null;

        String url = "jdbc:postgresql://100.116.29.49:5432/VTInstitute";
        String user = "ikero";
        String passwd = "ikero9090";

        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, user, passwd);
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to establish connection.");
            return null;
        }
    }


    // Function to test the database connection
    public boolean testConnection() {
        Connection conn = openConnection();
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        } else {
            return false;
        }
    }
}
