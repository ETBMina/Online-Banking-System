package com.company;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

enum statusLogin {WRONGID, WRONGPASSWORD, CORRECT}
public class DBController {

    private static Integer databaseSelector;

    public static void setDatabaseSelector(Integer databaseSelector) {
        DBController.databaseSelector = databaseSelector;
    }

    public static Connection createConnection() {
        Connection connection = null;
        try {
            if(databaseSelector==1)
            {
                connection = DriverManager.getConnection("jdbc:sqlite:server1DB.db");
            }
            else
            {
                connection = DriverManager.getConnection("jdbc:sqlite:server2DB.db");
            }
        } catch (SQLException e) {
            System.out.println("Something went wromg: fail in create connection" + e.getMessage());
        }
        return connection;
    }
    public static Statement createStatement(Connection connection) {
        Statement stmt = null;
        try {
            stmt  = connection.createStatement();
        } catch (SQLException e) {
            System.out.println("Something went wromg: fail in create statement " + e.getMessage());
        }
        return stmt;
    }

    public static synchronized int register (com.company.Account account) {
        try {
            Connection conn=createConnection();
            Statement stmt=createStatement(conn);
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
            ResultSet rs = stmt.executeQuery( "select * from Accounts WHERE ID=(SELECT MAX(ID) from Accounts)");

            while ( rs.next() ) {
                int ret=rs.getInt("ID");
                conn.close();
                    return ret;
            }
            conn.close();

            return -1;
        } catch (SQLException e) {
            System.out.println("Something went wrong: Cann't register" + e.getMessage());
            return -1;
        }


    }
    public static synchronized statusLogin  login(Account account) {
        Connection con=createConnection();
        Statement stmt=createStatement(con);
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
                con.close();
                return statusLogin.WRONGID;

            } else {
                if (!BCrypt.checkpw(Integer.toString(account.getPassword()), idResutlRet.getString(3))){
                    con.close();
                    return statusLogin.WRONGPASSWORD;
                }
                else {
                    account.setFull_name(idResutlRet.getString(2));
                    account.setBalance(idResutlRet.getInt(4));
                    con.close();
                    return statusLogin.CORRECT;
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Default
        return statusLogin.WRONGID;
    }


    public static synchronized void addToHistory(Transaction transaction)
    {
        Connection con=createConnection();
        Statement stmt= createStatement(con);
        String SQL="CREATE TABLE IF NOT EXISTS History " +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "AccountID INTEGER NOT NULL,"+
                "DESCRIPTION TEXT NOT NULL )";
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss").format(Calendar.getInstance().getTime());
        String transactionDescription=timeStamp+" ::"+transaction.toString();

        try {
            stmt.execute(SQL);
            SQL="INSERT INTO History(AccountID,DESCRIPTION) " +
                    "VALUES ('"+transaction.getSource()+"' , '"+transactionDescription+"')";
            stmt.execute(SQL);
            con.close();
        } catch (SQLException e) {
            System.out.println("Cannot Add Transaction To History " + e.getMessage());
        }

        return;
    }
    public static synchronized String viewHistory(Account account) throws SQLException {
        Connection con=createConnection();
        PreparedStatement idPreparedStatement = null;
        ResultSet idResutlRet = null;
        String idQuery = "SELECT * FROM History WHERE AccountID = ?";
        // Make idQuery as prepared statement
        idPreparedStatement = con.prepareStatement(idQuery);
        // Set ID Value
        idPreparedStatement.setInt(1, account.getUser_id());

        idResutlRet = idPreparedStatement.executeQuery();
        String string = new String();
        while (idResutlRet.next()) {
            string += (idResutlRet.getString(3) + "\n");
        }
        con.close();
        return string;
    }


    public synchronized static void editBalance(com.company.Account account)
    {
        Connection con=createConnection();
        Statement stmt= createStatement(con);
        String SQL="UPDATE Accounts SET BALANCE = '"+Integer.toString(account.getBalance())+ "'WHERE ID = '"+Integer.toString(account.getUser_id())+"'";
        try {
            stmt.execute(SQL);
            con.close();
        } catch (SQLException e) {
            System.out.println("Something went wrong:Cann't edit the balance " + e.getMessage());
        }

        return;
    }




    public static synchronized   Account readAccount(int id ) {
        Connection con=createConnection();
        Statement stmt=createStatement(con);
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
                con.close();
                return account;
            }
            else {
                int balance=idResutlRet.getInt("BALANCE");
                account.setBalance(balance);
                con.close();
                return account;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Default
        return account;
    }

    public  static  synchronized boolean doesAccountExist ( int id)
    {
        Connection con=createConnection();
        Statement stmt=createStatement(con);
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
                con.close();
                return false;
            }
            else {
                con.close();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Default
        return false;
    }




}