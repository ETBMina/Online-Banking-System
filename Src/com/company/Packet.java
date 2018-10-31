package com.company;

public class Packet {

    enum command {REGISTER, LOGIN, LOGOUT, BALANCE, OPERATION};

    private Account account;
    private Transaction transaction;
    private  command command ;

    public Packet(Account account, Transaction transaction, Packet.command command) {
        this.account = account;
        this.transaction = transaction;
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

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}







