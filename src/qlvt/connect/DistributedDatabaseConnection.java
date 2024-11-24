package qlvt.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class DistributedDatabaseConnection {

    public static final String SERVER1_URL = "jdbc:sqlserver://DESKTOP-9LSVPH7\\KYLE:1433;databaseName=VT_XAYDUNG;user=sa;password=12345;trustServerCertificate=true";
    public static final String SERVER2_URL = "jdbc:sqlserver://DESKTOP-9LSVPH7\\KYLE_CON1:1433;databaseName=VT_XAYDUNG;user=sa;password=12345;trustServerCertificate=true";
    public static final String SERVER3_URL = "jdbc:sqlserver://DESKTOP-9LSVPH7\\KYLE_CON2:1433;databaseName=VT_XAYDUNG;user=sa;password=12345;trustServerCertificate=true";
   // public static final String SERVER4_URL = "jdbc:sqlserver://DESKTOP-9LSVPH7\\KYLE_CON:1434;databaseName=VT_XAYDUNG;user=sa;password=12345;trustServerCertificate=true";

    // Kết nối đến một server cụ thể
    public Connection connectToServer(String serverUrl) throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(serverUrl);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("SQL Server JDBC Driver không tìm thấy.");
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(SERVER1_URL);
    }

    public Connection getConnection2() throws SQLException {
        return DriverManager.getConnection(SERVER2_URL);
    }

    // Kết nối đến tất cả các server trong hệ thống phân tán
    public Connection[] connectToAllServers() throws SQLException {
        Connection[] connections = new Connection[4];
        connections[0] = connectToServer(SERVER1_URL);
        connections[1] = connectToServer(SERVER2_URL);
        connections[2] = connectToServer(SERVER3_URL);
        //connections[3] = connectToServer(SERVER4_URL);
        return connections;
    }



}

