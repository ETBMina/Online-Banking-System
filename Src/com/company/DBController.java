package com.company;

import java.sql.*;

public class DBController {

    public static synchronized Connection createConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:testjava.db");
        } catch (SQLException e) {
            System.out.println("Something went wromg: " + e.getMessage());
        }
        return connection;
    }

    public static synchronized Statement createStatement(Connection connection) {
        Statement stmt = null;
        try {
            stmt  = connection.createStatement();
        } catch (SQLException e) {
            System.out.println("Something went wromg: " + e.getMessage());
        }
        return stmt;
    }

    public static synchronized boolean register (Account account,Statement stmt) {
        try {

            String sql = "CREATE TABLE IF NOT EXISTS Bank " +
                    "(ID INTEGER PRIMARY KEY     AUTOINCREMENT," +
                    " NAME           TEXT    NOT NULL, " +
                    " PASSWORD       INT     NOT NULL, " +
                    " BALANCE        INT     NOT NULL)";
            stmt.execute(sql);

            sql = "INSERT INTO BANK (NAME,PASSWORD,BALANCE) " +
                    "VALUES ('"+account.getFull_name()+"' , '"+
                    Integer.toString(account.getPassword()) +"' , '"+
                    Integer.toString(account.getBalance())+"')";
            ResultSet rs = stmt.executeQuery( "SELECT * FROM Bank WHERE NAME='"+
                    account.getFull_name()+"' AND PASSWORD='"+
                    Integer.toString(account.getPassword())+"' AND BALANCE='"+
                    Integer.toString(account.getBalance())+"'");

            while ( rs.next() ) {
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

    public static synchronized int login(Account account){
        return 0;
    }

    public static synchronized void addToHistory(Transaction transaction ){
        return;
    }

    public static synchronized String viewHistory(Account account){
        return "okay";
    }

    public static synchronized void editBalance(Account account){
        return;
    }

    public static synchronized Account readAccount ( int id)
    {
        Account he = new Account("mina", 13 ,13);
        return he;

    }
    public  static  synchronized boolean doesAccountExist ( int id){

        return false ;
    }
}
