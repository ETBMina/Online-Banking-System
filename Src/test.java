package com.company;

import sun.security.util.Password;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static com.company.DBController.*;

public class test {
    public static void main(String[] args) throws SQLException {
        // Connect to database
        Connection con =createConnection();
        // Create Statement
        Statement stm = createStatement(con);

        register(new Account("Ahmed",123,125), stm);


        Account loginAccount = new Account("Ahmed",122,123456);
        loginAccount.setUser_id(515);
        if(statusLogin.WRONGID==login(loginAccount,con,stm))
            System.out.println("Wrong id");
        else if(statusLogin.WRONGPASSWORD==login(loginAccount,con,stm))
            System.out.println("Wrong Password");
        else System.out.println("CORRECT");
        System.out.println("AccountID : "+loginAccount.getUser_id());
    }
}
