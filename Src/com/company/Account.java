package com.company;
import com.sun.net.httpserver.Authenticator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Statement;

import static com.company.errorType.SUCCESS;


enum errorType {  SOURSENOTEXIST , DESTINATIONNOTEXIST , VALUEISBIGGERTHANBALANCE  , SUCCESS , UNKNOWNERROR  }

public class Account implements Serializable {

    private int user_id;
    private int balance ;
    private String full_name ;
    private int password;

    public Account(int user_id, int password) {
        this.user_id = user_id;
        this.password = password;
    }


    public Account(int user_id, int password, int balance, String full_name) {
        this.user_id = user_id;
        this.password = password;
        this.balance = balance;
        this.full_name = full_name;
    }


    public Account(){}

    public Account(String full_name, int password, int balance) {
        this.password = password;
        this.balance = balance;
        this.full_name = full_name;
    }


    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public static synchronized errorType editBalance (Transaction  trans )
    {
        errorType errorType ;
        if(DBController.doesAccountExist(trans.getSource() ) != true  )
        {
            // account does not exist transaction failed
            return com.company.errorType.SOURSENOTEXIST ;
        }
        Account source = new Account();
        Account destination = new Account();
        source = DBController.readAccount(trans.getSource());
        switch (trans.getOperation())
        {
            case WITHDRAW:
                if(trans.getValue()>source.getBalance())
                {
                    //transaction failed
                    return com.company.errorType.VALUEISBIGGERTHANBALANCE;
                }
                //transaction succeded
                source.setBalance(source.getBalance()-trans.getValue());
                DBController.editBalance(source);
                DBController.addToHistory(trans);
                return SUCCESS;
            case DEPOSIT:
                //making the transaction
                source.setBalance(source.getBalance()+trans.getValue());
                //updating the data base
                DBController.editBalance(source);
                DBController.addToHistory(trans);
                return SUCCESS;
            case TRANSFERTOSAMEBANK:
                if(DBController.doesAccountExist(trans.getDestination()) != true  )
                {
                    // account does not exist transaction failed
                    return com.company.errorType.DESTINATIONNOTEXIST ;
                }
                if(trans.getValue()>source.getBalance())
                {
                    //transaction failed
                    return com.company.errorType.VALUEISBIGGERTHANBALANCE;
                }
                //transaction succeded
                destination = DBController.readAccount(trans.getDestination());
                //making the transaction
                destination.setBalance(destination.getBalance()+trans.getValue());
                source.setBalance(source.getBalance()-trans.getValue());
                //System.out.println("Source Balance : "+source.getBalance());
                //System.out.println("Destination Balance :"+destination.getBalance());
                //updating the data base
                DBController.editBalance(source);
                DBController.editBalance(destination);
                DBController.addToHistory(trans);
                return SUCCESS;
            case TRANSFERTOANOTHERBANK:
                if(trans.getValue()>source.getBalance())
                {
                    //transaction failed
                    return com.company.errorType.VALUEISBIGGERTHANBALANCE;
                }
                //transaction can succeded from the end of this server we now contact the other server
                trans.setOperation(Transaction.operation.DEPOSITFROMANOTHERBANK);
                //connect to other server and get his response
                PaymentServer.connectToOtherServer();
                boolean answerFromOtherServer = PaymentServer.sendPacketToOtherServerAndGetResponse(trans);
                if(answerFromOtherServer==false)
                {
                    //account at destenation does not exist
                    return com.company.errorType.DESTINATIONNOTEXIST;
                }
                //other wise the transfer occured
                //so we need to update our source her and add it her to our history
                source.setBalance(source.getBalance()-trans.getValue());
                DBController.editBalance(source);
                DBController.addToHistory(trans);
                break;
            case DEPOSITFROMANOTHERBANK:
                if(DBController.doesAccountExist(trans.getDestination()  ) != true  )
                {
                    // account does not exist transaction failed
                    return com.company.errorType.DESTINATIONNOTEXIST ;
                }
                destination = DBController.readAccount(trans.getDestination());
                destination.setBalance(destination.getBalance()+trans.getValue());
                DBController.editBalance(destination);
                DBController.addToHistory(trans);
                return SUCCESS ;
        }
        return com.company.errorType.UNKNOWNERROR;
    }

    public static synchronized boolean editBalanceforotherserver (Transaction  trans )
    { return true; }

}