package com.company;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
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
            String data = readFileAsString("Server.txt");
            String portNumber = data.substring(10,14);
            String serverIp = data.substring(0,9);
            Socket c = new Socket(serverIp, Integer.parseInt(portNumber));

            //open comm
            ObjectOutputStream os = new ObjectOutputStream(c.getOutputStream());
            ObjectInputStream  is = new ObjectInputStream (c.getInputStream()) ;
            //    DataOutputStream dos = new DataOutputStream(c.getOutputStream());
            //    DataInputStream dis = new DataInputStream(c.getInputStream());

            //perform I/O
            Packet packetToSend = new Packet();
            ServerResponse packetToRecive = new ServerResponse();
            //the program itself
            System.out.println("Welcome to our Client Payment Application");
            outer :
            while (true)
            {

                //welcome user
                System.out.println("enter 1 for log in");
                System.out.println("enter 2 for register");
                System.out.println("enter 3 to close applicarion");

                //taking input and checking it is right
                String userResp = sc.nextLine();
                if(!checkUserIpForFirstOption(userResp))
                {
                    System.out.println("error in your option try again ");
                    continue outer;
                }


                switch (userResp)
                {
                    case "1" :
                        //taking id and checking for error
                        System.out.println("Enter your USER ID :");
                        String userId = sc.nextLine();
                        if(!checkUserId(userId)) {
                            System.out.println("INVALID USER ID .");
                            continue outer;
                        }
                        //taking password in and checking for error
                        System.out.println("Enter your PASSWORD :");
                        String password = sc.nextLine();
                        if(!checkUserPassword(password)) {
                            System.out.println("INVALID PASSWORD .");
                            continue outer;
                        }

                        //sending to server
                        System.out.println("please hold while we contact the server.");
                        packetToSend =
                         new Packet( new Account(Integer.parseInt(userId),Integer.parseInt(password)) , Packet.command.LOGIN  );
                        os.writeObject(packetToSend);

                        //reciving packet response
                        packetToRecive =(ServerResponse) is.readObject();
                        System.out.println(packetToRecive.getResponse());
                        if(packetToRecive.isSucces())
                            while(true)
                            {








                            //////////////////////////////////////////////here//////////////////////////////////









                            }

                            continue outer;
                    case "2" :
                        //taking in full name
                        System.out.println("Enter your FULLNAME : ");
                        String fullName = sc.nextLine();

                        //taking in password
                        String pass = "";
                        do {
                            System.out.println("Enter your PASSWORD : (Must consist of 4 digits only)  ");
                            pass = sc.nextLine();
                            if (!checkUserPassword(pass))
                                System.out.println("INVALID PASSWORD  try  again");
                        }while(!checkUserPassword(pass));

                        //taking in balance
                        String balance ="" ;
                        do {
                            System.out.println("Enter your BALANCE : ");
                            balance = sc.nextLine();
                            if (!checkUserId(balance))
                                System.out.println("INVALID BALANCE  try  again");
                        }while(!checkUserId(balance));

                        //sending packet
                        packetToSend = new Packet( new Account(fullName ,Integer.parseInt(pass), Integer.parseInt(balance)) , Packet.command.REGISTER  );
                        os.writeObject(packetToSend);
                        //   String serverResponse1 = dis.readUTF();

                        //reciving response
                        packetToRecive =(ServerResponse) is.readObject();
                        System.out.println(packetToRecive.getResponse());
                        continue outer ;
                    case "3":
                        packetToSend = new Packet(Packet.command.LOGOUT);
                        os.writeObject(packetToSend);
                        System.out.println("come visit us again soon BYE. ");
                        break outer;
                }
            }

            System.exit(0) ;
            //close comm
            os.close();
            is.close();
           // dis.close();
           // dos.close();
            //close socket for client
            c.close();
        }
        catch (IOException ex)
        {
         //3
            //   System.out.println("config file does not exist please confirm with our support team");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static String readFileAsString(String fileName)throws Exception
    {
        String data = "";
        data = new String(Files.readAllBytes(Paths.get(fileName)));
        return data;
    }
    public static boolean checkUserIpForFirstOption ( String ip )
    {
        if(ip.length()!=1)
            return false;
        char x = ip.charAt(0);
        if (x>48&&x<=51)
            return true ;
        return false ;
    }
    public static boolean checkUserPassword(String password)
    {
        if(password.length()!=4)
            return false;
        char x;
        for (int i=0;i<=3;i++)
        {
            x = password.charAt(i);
            if (!Character.isDigit(x))
                return false;
        }
        return true ;
    }
    public static boolean checkUserId(String userId)
    {
        char x;
        for (int i=0;i<=userId.length()-1;i++)
        {
            x = userId.charAt(i);
            if (!Character.isDigit(x))
                return false;
        }
        return true ;
    }
}
