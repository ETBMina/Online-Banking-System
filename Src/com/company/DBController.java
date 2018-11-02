package com.company;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

enum statusLogin {WRONGID, WRONGPASSWORD, CORRECT}
public class DBController {

    public static Connection createConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
        } catch (SQLException e) {
            System.out.println("Something went wromg: " + e.getMessage());
        }
        return connection;
    }
    public static Statement createStatement(Connection connection) {
        Statement stmt = null;
        try {
            stmt  = connection.createStatement();
        } catch (SQLException e) {
            System.out.println("Something went wromg: " + e.getMessage());
        }
        return stmt;
    }

    public static int register (com.company.Account account, Statement stmt) {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS Accounts " +
                    "(ID INTEGER PRIMARY KEY     AUTOINCREMENT," +
                    " NAME           TEXT    NOT NULL, " +
                    " PASSWORD       TEXT     NOT NULL, " +
                    " BALANCE        INT     NOT NULL)";
            stmt.execute(sql);
            String hashedPass= BCrypt.hashpw(Integer.toString(account.getPassword()), BCrypt.gensalt(12));
            sql = "INSERT INTO Accounts (NAME,PASSWORD,BALANCE) " +
                    "VALUES ('"+account.getFull_name()+"' , '"+
                    hashedPass +"' , '"+
                    Integer.toString(account.getBalance())+"')";
            stmt.execute(sql);
           /* ResultSet rs = stmt.executeQuery( "SELECT * FROM Accounts WHERE NAME='"+
                    account.getFull_name()+"' AND BALANCE='"+
                    Integer.toString(account.getBalance())+"'");*/
            ResultSet rs = stmt.executeQuery( "select * from Accounts WHERE ID=(SELECT MAX(ID) from Accounts)");

            while ( rs.next() ) {
              //  if (BCrypt.checkpw(Integer.toString(account.getPassword()), hashedPass))
                    return rs.getInt("ID");
            }


            return -1;
        } catch (SQLException e) {
            System.out.println("Something went wrong: " + e.getMessage());
            return -1;
        }


    }
    public static statusLogin login(Account account, Connection con, Statement stmt) {
        PreparedStatement idPreparedStatement = null;
        ResultSet idResutlRet = null;
        String idQuery = "SELECT * FROM Accounts WHERE ID = ?";
        try {
            // Make idQuery as prepared statement
            idPreparedStatement = con.prepareStatement(idQuery);
            // replace the first Question Mark ?
            idPreparedStatement.setInt(1, account.getUser_id());
            // Execute the query
            idResutlRet = idPreparedStatement.executeQuery();
            if (!idResutlRet.next()) {
                return statusLogin.WRONGID;
            } else {
                if (!BCrypt.checkpw(Integer.toString(account.getPassword()), idResutlRet.getString(3)))
                    return statusLogin.WRONGPASSWORD;
                else {
                    account.setFull_name(idResutlRet.getString(2));
                    account.setBalance(idResutlRet.getInt(4));
                    return statusLogin.CORRECT;
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Default
        return statusLogin.WRONGID;
    }


    public static void addToHistory(Transaction transaction,Connection conn )
    {
        Statement stmt= createStatement(conn);
        String SQL="CREATE TABLE IF NOT EXISTS History " +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "DESCRIPTION TEXT NOT NULL )";
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss").format(Calendar.getInstance().getTime());
        String transactionDescription=timeStamp+" ::"+transaction.toString();

        try {
            stmt.execute(SQL);
            SQL="INSERT INTO History(DESCRIPTION) " +
                    "VALUES ('"+transactionDescription+"')";
            stmt.execute(SQL);
        } catch (SQLException e) {
            System.out.println("Cannot Add Transaction To History " + e.getMessage());
        }
        return;
    }
    public static String viewHistory(Account account, Connection con) throws SQLException {
        PreparedStatement idPreparedStatement = null;
        ResultSet idResutlRet = null;
        String idQuery = "SELECT * FROM History WHERE ID = ?";
        // Make idQuery as prepared statement
        idPreparedStatement = con.prepareStatement(idQuery);
        // Set ID Value
        idPreparedStatement.setInt(1, account.getUser_id());

        idResutlRet = idPreparedStatement.executeQuery();
        String string = new String();
        while (idResutlRet.next()) {
            string += (idResutlRet.getString(1) + "\n");
        }
        return string;
    }


    public synchronized static void editBalance(com.company.Account account,Connection conn)
    {
        Statement stmt= createStatement(conn);
        String SQL="UPDATE Accounts SET BALANCE = '"+Integer.toString(account.getBalance())+ "'WHERE ID = '"+Integer.toString(account.getUser_id())+"'";
        try {
            stmt.execute(SQL);
        } catch (SQLException e) {
            System.out.println("Something went wrong:x " + e.getMessage());
        }

        return;
    }






    public static synchronized   Account readAccount(int id , Connection con, Statement stmt) {

        Account account = new Account();
        account.setUser_id(id);

        PreparedStatement idPreparedStatement = null;
        ResultSet idResutlRet = null;
        String idQuery = "SELECT * FROM Accounts WHERE ID = ?";
        try {
            // Make idQuery as prepared statement
            idPreparedStatement = con.prepareStatement(idQuery);
            // replace the first Question Mark ?
            idPreparedStatement.setInt(1, account.getUser_id());
            // Execute the query
            idResutlRet = idPreparedStatement.executeQuery();

            if (!idResutlRet.next()) {
                return account;
            }
            else {
                int balance=idResutlRet.getInt("BALANCE");
                account.setBalance(balance);
                return account;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Default
        return account;
    }

    public  static  synchronized boolean doesAccountExist ( int id, Connection con, Statement stmt)
    {
        Account account = new Account();
        account.setUser_id(id);
        PreparedStatement idPreparedStatement = null;
        ResultSet idResutlRet = null;
        String idQuery = "SELECT * FROM Accounts WHERE ID = ?";
        try {
            // Make idQuery as prepared statement
            idPreparedStatement = con.prepareStatement(idQuery);
            // replace the first Question Mark ?
            idPreparedStatement.setInt(1, account.getUser_id());
            // Execute the query
            idResutlRet = idPreparedStatement.executeQuery();

            if (!idResutlRet.next()) {
                return false;
            }
            else {
                //   int balance=idResutlRet.getInt("BALANCE");
                // account.setBalance(balance);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Default
        return false;
    }




}