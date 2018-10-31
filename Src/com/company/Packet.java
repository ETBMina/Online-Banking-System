package com.company;

public class Packet {

    private Account account;
    private Transaction transaction;

    private enum command {REGISTER, LOGIN, LOGOUT, BALANCE, OPERATION};

    public Packet(Account account, Transaction transaction) {
        this.account = account;
        this.transaction = transaction;
    }

    public Account getAccount() {

        return account;
    }

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







