package jdbc.database;
import java.sql.*;

public class Db {
    private static Connection conn = null;

    public static Connection getConnection() {
        try {
            if (conn == null) {
                final String username = "root";
                final String password = "Cadu2099$#";
                final String url = "jdbc:mysql://localhost:3306/coursejdbc?useSSL=false";
                conn = DriverManager.getConnection(url, username, password);
            }
        } catch (SQLException e) {
            throw new DB_Exception("SQL ERROR!");
        }
        return conn;
    }

    public static void closeConnection(){
        if (conn!= null){
            try {
                conn.close();

                if (conn == null){
                    System.out.println("Connection Closed");
                }
            }
            catch (SQLException e){
                throw new DB_Exception("SQL ERROR!");
            }
        }
    }

    public static void closeStatement(Statement statement){
        if(statement != null){
            try {
                statement.close();
            } catch (SQLException e) {
                throw new DB_Exception("Statement Close ERROR!");
            }
        }
    }

    public static void closeResultSet(ResultSet resultSet){
        if(resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new DB_Exception("ResultSet Close ERROR!");
            }
        }
    }
}

