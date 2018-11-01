package com.company;

import java.io.Serializable;

public class Transaction implements Serializable {

    enum operation {   WITHDRAW  , DEPOSIT , DEPOSITFROMANOTHERBANK, TRANSFERTOSAMEBANK , TRANSFERTOANOTHERBANK    } ;

    private int source ;
    private int destination ;
    private int value ;
    private operation operation ;

    public Transaction() { }

    public Transaction(int source, int destination, int value, Transaction.operation operation) {
        this.source = source;
        this.destination = destination;
        this.value = value;
        this.operation = operation;
    }

    public int getSource() { return source; }

    public void setSource(int source) { this.source = source; }

    public int getDestination() { return destination; }

    public void setDestination(int destination) { this.destination = destination; }

    public int getValue() { return value; }

    public void setValue(int value) { this.value = value; }

    public Transaction.operation getOperation() { return operation; }

    public void setOperation(Transaction.operation operation) { this.operation = operation; }

    public String toString ()
    {
        String printStirng = "";
        switch (operation)
        {
            case WITHDRAW:
                printStirng = "Account no. = "+ Integer.toString(source)+"Withdraw an amount of money of Value = "+Integer.toString(value) +"from his account.";
                break;
            case DEPOSIT:
                printStirng = "Account no. = "+ Integer.toString(source)+"Deposited an amount of money of Value = "+Integer.toString(value) +"to his account.";
                break;
            case TRANSFERTOSAMEBANK:
                case TRANSFERTOANOTHERBANK:
                    case DEPOSITFROMANOTHERBANK:
                printStirng = "Account no. = "+ Integer.toString(source)+"Deposited an amount of money of Value = "+Integer.toString(value) +"to Account no. = "+Integer.toString(source)+"  ." ;
                break;
        }

        return  printStirng ;
    }


}