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
            //create socket
            ServerSocket s = new ServerSocket(1234);
            // number of clients
            int n = 1 ;

            System.out.println("the server is ready to accept customers ");

            while (true)
            {
                Socket c = s.accept();
                System.out.println("Client Arrived N.O." + n);
                ClientHandler ch = new ClientHandler(c);
                //handle client in parrallel
                Thread t = new Thread(ch);
                t.start();
                //create new light weight process
                //and run in parallel and the main thread
                //continues
                n++;
            }
            //s.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }




















    }


    public static void connectToOtherServer()
    {

    }
    public static boolean sendPacketToOtherServerAndGetResponse( Transaction trans )
    {

        return true ;
    }





}
