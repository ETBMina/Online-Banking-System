package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import static com.company.DBController.createConnection;
import static com.company.DBController.createStatement;
import static com.company.DBController.register;

public class PaymentServer
{
    public static void main(String[] args)
    {
        try
        {
            //create socket
            String data = readFileAsString("ServerPortNo.txt");
            ServerSocket s = new ServerSocket(Integer.parseInt(data));
            //ServerSocket s = new ServerSocket(1234);
            // number of clients
            int n = 1 ;
            //greating at server
            System.out.println("The server is ready to accept clients ");

            while (true)
            {
                Socket c = s.accept();
                System.out.println("A Client just arrived ");
                ClientHandler ch = new ClientHandler(c);
                //handle clients in parallel
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
        } catch (Exception e) {
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
    public static String readFileAsString(String fileName)throws Exception
    {
        String data = "";
        data = new String(Files.readAllBytes(Paths.get(fileName)));
        return data;
    }

}