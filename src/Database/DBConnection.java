package Database;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * connection to the database
 */
public class DBConnection {
    
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
//    private static final String ipAddress = "//wgudb.ucertify.com:3306/";
    private static final String ipAddress = "//localhost:3306/";
    private static final String dbName = "client_schedule";

    private static final String jdbcURL = protocol + vendorName + ipAddress + dbName;
    
    private static final String MYSQLJBCDriver = "com.mysql.cj.jdbc.Driver";
    
    private static final String username = "sqlUser";
    private static Connection conn = null;
    private static final String Password = "Passw0rd!";

//    public static void makeConnection() throws SQLException {
//        conn = (Connection) DriverManager.getConnection(jdbcURL, username, Password);
//    }

    public static void startConnection() {
        System.out.print(jdbcURL);
        try {
            Class.forName(MYSQLJBCDriver);
            conn = DriverManager.getConnection(jdbcURL, username, Password);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public static Connection getConnection() {
        return conn;
    }

    public static void closeConnection() {
        try {
            conn.close();
        }
        catch (Exception e) {
            //do nothing
        }
    }
}
