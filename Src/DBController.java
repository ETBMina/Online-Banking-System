package com.company;

import java.sql.*;


public class DBController {
    public static final int WRONGID = 0;
    public static final int WRONGPASSWORD = 1;
    public static final int CORRECT = 2;

    public static Connection createConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:testjava.db");
        } catch (SQLException e) {
            System.out.println("Something went wromg: " + e.getMessage());
        }
        return connection;
    }

    public static Statement createStatement(Connection connection) {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
        } catch (SQLException e) {
            System.out.println("Something went wromg: " + e.getMessage());
        }
        return stmt;
    }

    public static boolean register(Account account, Statement stmt) {
        try {

            String sql = "CREATE TABLE IF NOT EXISTS Bank " +
                    "(ID INTEGER PRIMARY KEY     AUTOINCREMENT," +
                    " NAME           TEXT    NOT NULL, " +
                    " PASSWORD       INT     NOT NULL, " +
                    " BALANCE        INT     NOT NULL)";
            stmt.execute(sql);

            sql = "INSERT INTO BANK (NAME,PASSWORD,BALANCE) " +
                    "VALUES ('" + account.getFull_name() + "' , '" +
                    Integer.toString(account.getPassword()) + "' , '" +
                    Integer.toString(account.getBalance()) + "')";
            ResultSet rs = stmt.executeQuery("SELECT * FROM Bank WHERE NAME='" +
                    account.getFull_name() + "' AND PASSWORD='" +
                    Integer.toString(account.getPassword()) + "' AND BALANCE='" +
                    Integer.toString(account.getBalance()) + "'");

            while (rs.next()) {
                int id = rs.getInt("ID");
                account.setUser_id(id);
                //System.out.println(Integer.toString(id));
            }
            stmt.execute(sql);

            return true;
        } catch (SQLException e) {
            System.out.println("Something went wrong: " + e.getMessage());
            return false;
        }


    }



    public static int login(Account account) throws SQLException {
        PreparedStatement idPreparedStatement = null, passwordPreparedStatement = null;
        ResultSet idResutlRet = null, passwordResultSet = null;
        String idQuery = "SELECT * FROM Bank WHERE ID = ?";
        String passwordQuery = "SELECT * FROM Bank WHERE PASSWORD = ?";
        try {
            // Create connection
            Connection con = createConnection();
            // Make idQuery as prepared statement
            idPreparedStatement = con.prepareStatement(idQuery);
            passwordPreparedStatement = con.prepareStatement(passwordQuery);
            // replace the first Question Mark ?
            idPreparedStatement.setInt(1, account.getUser_id());
            passwordPreparedStatement.setInt(1, account.getPassword());
            // Execute the query
            idResutlRet = idPreparedStatement.executeQuery();
            passwordResultSet = passwordPreparedStatement.executeQuery();
            if (!idResutlRet.next())
                return DBController.WRONGID;
            else if (!passwordResultSet.next())
                return DBController.WRONGPASSWORD;
            else
                return DBController.CORRECT;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            idPreparedStatement.close();
            passwordPreparedStatement.close();
            idResutlRet.close();
            passwordResultSet.close();
        }
        return 0;
    }

    public static void addToHistory(Transaction transaction) {
        return;
    }

    public static String viewHistory(Account account) throws SQLException {
        PreparedStatement idPreparedStatement = null;
        ResultSet idResutlRet = null;
        String idQuery = "SELECT * FROM History WHERE ID = ?";
        // Create connection
        Connection con = createConnection();
        // Make idQuery as prepared statement
        idPreparedStatement = con.prepareStatement(idQuery);
        // Set ID Value
        idPreparedStatement.setInt(1, account.getUser_id());

        idResutlRet = idPreparedStatement.executeQuery();
        String string=new String();
        while(idResutlRet.next()) {
           /* string+=(idResutlRet.getString(1) + "\t" +
                    idResutlRet.getString(2) + "\t" +
                    idResutlRet.getInt(3) + "\t" +
                    idResutlRet.getInt(4)+"\n");*/
        }
        return string;
    }

    public static void editBalance(Account account) {
        return;
    }
}
