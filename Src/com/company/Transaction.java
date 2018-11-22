package com.company;

import java.io.Serializable;

public class Transaction implements Serializable {

    enum operation {   WITHDRAW  , DEPOSIT , TRANSFERTOSAMEBANK , TRANSFERTOANOTHERBANK    } ;

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
                if(source!=destination)
                {
                    printStirng = "Account no. = "+ Integer.toString(source)+" transfer an amount of money of value = "+Integer.toString(value) +" from his account to account no. ="+Integer.toString(destination)+"in another bank.";
                    break;
                }
                printStirng = "Account no. = "+ Integer.toString(source)+" withdrawn an amount of money of value = "+Integer.toString(value) +" from his account.";
                break;
            case DEPOSIT:
                printStirng = "Account no. = "+ Integer.toString(source)+" deposited an amount of money of value = "+Integer.toString(value) +" to his account.";
                break;
            case TRANSFERTOSAMEBANK:
                printStirng = "Account no. = "+ Integer.toString(source)+" deposited an amount of money of value = "+Integer.toString(value)
                        +" to account no. = "+Integer.toString(destination);
                break;
            case TRANSFERTOANOTHERBANK:
                printStirng = "Account no. = "+ Integer.toString(destination)+" deposited an amount of money of value = "+Integer.toString(value) +" to account no. = "+Integer.toString(source)+"." ;
                break;
        }

        return  printStirng ;
    }


}