package com.company;

import java.io.Serializable;

public class Packet implements Serializable {

    enum command {REGISTER, LOGIN, LOGOUT, BALANCE, OPERATION,VIEWHISTORY};

    private Account account;
    private Transaction transaction;
    private  command command ;
    private Boolean fromServer=false;

    public Packet(Account account, Transaction transaction, Packet.command command,Boolean fromServer) {
        this.account = account;
        this.transaction = transaction;
        this.command = command;
        this.fromServer=fromServer;
    }
    public Packet(Account account, Transaction transaction, Packet.command command) {
        this.account = account;
        this.transaction = transaction;
        this.command = command;
    }

    public Packet() { }

    public Packet(Account account, Packet.command command) {
        this.account = account;
        this.command = command;
    }
    public Packet(Account account, Packet.command command,Boolean fromServer) {
        this.account = account;
        this.command = command;
        this.fromServer=fromServer;
    }



    public Packet( Packet.command command) {
        this.command = command;
    }


    public Packet.command getCommand() { return command; }

    public void setCommand(Packet.command command) { this.command = command; }

    public Account getAccount() { return account; }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public Boolean getFromServer() {
        return fromServer;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}













