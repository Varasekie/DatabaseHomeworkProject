package db;

import java.sql.*;

public class db {
    private Connection dbConn;
    private Statement statement;

    private int condition = 0;

    public db(int i) {
        String driverName = "com.mysql.cj.jdbc.Driver";
        String dbURL = "jdbc:mysql://localhost:3306/estateagency?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
        String userName = "root";
        String userPwd = "root";
        //0是普通用户，1是管理员
        this.condition = i;
        if (condition == 0) {
            userName = "ordinary";
            userPwd = "ordinary";
        }
        if (condition == 1) {
            userName = "root";
            userPwd = "root";
        }

        //2是游客
        if (condition == 2) {
            userName = "tourists";
            userPwd = "tourists";
        }

        try {
            Class.forName(driverName);
            dbConn = DriverManager.getConnection(dbURL, userName, userPwd);
//            System.out.println("success");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public db() {
        this(0);
    }

    private int executeUpdate(String sql) throws SQLException {
        statement = dbConn.createStatement();
        return statement.executeUpdate(sql);

    }

    public ResultSet executeQuery(String sql) throws SQLException {
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

    public CallableStatement prepareCall(String sql) throws SQLException {
        return dbConn.prepareCall(sql);
    }

    public static void main(String[] args) {
        db db = new db();
//        db.db.dbConn.close();

    }
}
