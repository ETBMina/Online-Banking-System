package com.company;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class PaymentClient
{

    public static void main(String[] args)
    {

        try
        {
            //connect
            //create socket
            Scanner sc = new Scanner(System.in);
            Socket c = new Socket("127.0.0.1", 1234);

            ObjectOutputStream os = new ObjectOutputStream(c.getOutputStream());
            ObjectInputStream  is = new ObjectInputStream (c.getInputStream()) ;
            //perform IO
            while (true)
            {
                Account account=new Account("Ahmed",123,125);
                String userresp = sc.nextLine();
                os.writeObject(account);
                if(userresp.equalsIgnoreCase("N"))
                    break;
            }
            //close comm
            os.close();
            is.close();
            //close socket for client
            c.close();
        }
        catch (IOException ex)
        {
            System.out.println("wrong input ");
        }
    }







}
