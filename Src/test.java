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


        Account loginAccount = new Account("Ahmed",1223,123456);
        loginAccount.setUser_id(1);
        if(0==login(loginAccount))
            System.out.println("Wrong id");
        if(1==login(loginAccount))
            System.out.println("Wrong Password");
        System.out.println("AccountID : "+loginAccount.getUser_id());
        System.out.println(login(loginAccount));
    }
}
