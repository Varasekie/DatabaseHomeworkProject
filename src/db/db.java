package db;

import java.sql.*;

public class db {
    private Connection dbConn;
    private Statement statement;

    public db(){
        String driverName = "com.mysql.cj.jdbc.Driver";
        String dbURL = "jdbc:mysql://localhost:3306/estateagency?&useSSL=false&serverTimezone=UTC";

        String userName = "root";
        String userPwd = "root";

        try {
            Class.forName(driverName);
            dbConn = DriverManager.getConnection(dbURL,userName,userPwd);
//            System.out.println("success");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private int executeUpdate(String sql) throws SQLException{
        statement = dbConn.createStatement();
        return statement.executeUpdate(sql);

    }

    public ResultSet executeQuery(String sql) throws SQLException{
        statement = dbConn.createStatement();
        return statement.executeQuery(sql);

    }

    public PreparedStatement preparedStatement(String sql) throws SQLException {
        return dbConn.prepareStatement(sql);
    }

    public void closeConn() throws SQLException {
        statement.close();
        dbConn.close();
    }

    public static void main(String[] args) {
        db db = new db();
//        db.db.dbConn.close();

    }
}
