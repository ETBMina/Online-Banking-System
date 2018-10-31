package com.company;

public class Transaction {

    private int source ;
    private int destination ;
    private int value ;
    private enum operation {  WITHDRAW  , DEPOSIT, TRANSFERTOSAMEBANK , TRANSFERTOANOTHERBANK   } ;

    public Transaction(int source, int destination, int value) {
        this.source = source;
        this.destination = destination;
        this.value = value;
    }


    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
    public String toString ()
    {
        String Print_Stirng = "";
        if (source==destination)
            if (value>0)
                Print_Stirng =" "+ Integer.toString(value) + " was  Deposited into Account NO = " + Integer.toString(source)  + " ";
            else
                Print_Stirng =" "+ Integer.toString((value*-1)) + " was  Withdrawn from Account NO = " + Integer.toString(source)  + " ";
        else
                Print_Stirng =" "+ Integer.toString(value) + " was  Deposited into Account NO = " + Integer.toString(destination)
                        + " from account No = "    +Integer.toString(source)+" ";


        return  Print_Stirng;


    }


}