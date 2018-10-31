package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class PaymentServer
{

    public static void main(String[] args)
    {
        try
        {
            //1.Listen 
            //2.accept
            //3.create socket (I/O) with client
            ServerSocket s = new ServerSocket(1234);
            while (true)
            {
                Socket c = s.accept();
                System.out.println("Client Arrived");
                /*DataOutputStream dos = new DataOutputStream(c.getOutputStream());
                DataInputStream dis = new DataInputStream(c.getInputStream());*/
                ObjectOutputStream os=new ObjectOutputStream(c.getOutputStream());
                ObjectInputStream is=new ObjectInputStream(c.getInputStream());
                Account account;
                //4.perform IO with client
                while (true)
                {
                    account=(Account)is.readObject();
                    System.out.println(account.getBalance());
                  /*  dos.writeUTF("Hello enter account num:");
                    String accnum = dis.readUTF();
                    //check using accnum in DB
                    dos.writeUTF("Account num :"+accnum+ "is valid"
                            + "enter password ");
                    String pass = dis.readUTF();
                    //check using accnum and pass in DB
                    dos.writeUTF("Account num :"+accnum+ "and pass:"
                            +pass+"are valid enter payment ");
                    String payment = dis.readUTF();
                    //check if balance is enough
                    dos.writeUTF("Payment = "+ payment + "is successfull"
                            + "do you want to perform another y/n?");
                    
                    String userChoice = dis.readUTF();
                    if(userChoice.equalsIgnoreCase("N"))
                        break;
                    */
                }

                //5. close comm with client
               /* dos.close();
                dis.close();*/


            }

            //s.close();
        } catch (IOException ex)
        {
            System.out.println("Something went wrong");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}
