package com.company;

import java.sql.SQLException;
import java.sql.Statement;

import static com.company.DBController.*;

public class Client_Main {
    public static void main(String[] args) {

        Account Mina=new Account("Mina Talaat",123,4600);
        //Statement stmt= createStatement(createConnection());
        //register(Mina,stmt);
        //editBalance(Mina);
        /*try {
            stmt.execute("UPDATE Accounts SET BALANCE='6000' WHERE ID =1;");
        } catch (SQLException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }*/

        /*
        Transaction t1=new Transaction(1,2,3000,operation.WITHDRAW);
        addToHistory(t1);
        */
    }
}
