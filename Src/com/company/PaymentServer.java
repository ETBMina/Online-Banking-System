package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class PaymentServer
{
    public static void main(String[] args)
    {
        try
        {
            File file=new File("Server.txt");
            Scanner fileScanner=new Scanner(file);
            int portNumber1=Integer.parseInt(fileScanner.nextLine());
            String serverIp1=fileScanner.nextLine();
            int portNumber2=Integer.parseInt(fileScanner.nextLine());
            String serverIp2=fileScanner.nextLine();
            Scanner sysScanner=new Scanner(System.in);
            ServerSocket s;
            int serverNo;
            outer :
            while(true)
            {
                System.out.println("Enter 1 for first server, or 2 for the second server");
                serverNo=sysScanner.nextInt();
                if(serverNo==1)
                {
                    s = new ServerSocket(portNumber1);
                    ClientHandler.setServerNo(1);
                    break ;
                }
                else if(serverNo==2)
                {
                    s = new ServerSocket(portNumber2);
                    ClientHandler.setServerNo(2);
                    break;
                }
                else
                {
                    System.out.println("Wrong Choice");
                    continue outer;
                }
            }

            DBController.setDatabaseSelector(serverNo);
            //create socket
            //String data = readFileAsString("ServerPortNo.txt");
            //ServerSocket s = new ServerSocket(Integer.parseInt(data));
            //ServerSocket s = new ServerSocket(1234);
            // number of clients
            int n = 1 ;
            //greeting at server
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