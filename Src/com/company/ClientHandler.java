package com.company;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.*;

import static com.company.DBController.createConnection;
import static com.company.DBController.createStatement;
import static com.company.DBController.editBalance;


public class ClientHandler  implements Runnable
{
    Socket c;
    public ClientHandler(Socket c)
    {
        this.c = c;
    }
    @Override
    public void run( )
    {
        try
        {
            //i/o

            ObjectOutputStream os = new ObjectOutputStream(c.getOutputStream());
            ObjectInputStream  is = new ObjectInputStream(c.getInputStream());
            //    DataOutputStream dos = new DataOutputStream(c.getOutputStream());
            //    DataInputStream dis = new DataInputStream(c.getInputStream());

            //to connect to DB
            /*Connection conn=createConnection();
            Statement stmt = createStatement(conn);*/
            Packet recivedPacket = new Packet();
            ServerResponse serverResponsed = new ServerResponse();

            //program
            outer:
            while (true)
            {
                recivedPacket =(Packet) is.readObject();
                switch (recivedPacket.getCommand())
                {
                    case REGISTER:
                        int id = DBController.register(recivedPacket.getAccount());
                        if(id!=-1)
                        {
                            serverResponsed = new ServerResponse(  "you are now Registered and your id is "
                                    + id , true );
                            os.writeObject(serverResponsed);
                            //          dos.writeUTF("congratulations you have successfully registered");
                        }
                        else
                        {
                            serverResponsed = new ServerResponse("registration failed", false);
                            os.writeObject(serverResponsed);
                            //dos.writeUTF("your registration was NOT successful");;
                        }
                        continue outer;
                    case LOGIN:
                        statusLogin sLI = DBController.login(recivedPacket.getAccount());
                        switch (sLI)
                        {
                            case WRONGID:
                                // dos.writeUTF("WRONG ID TRY TO LOG AGAIN");
                                serverResponsed = new ServerResponse(  "WRONG ID TRY TO LOG AGAIN" , false );
                                os.writeObject(serverResponsed);
                                continue outer;
                            case CORRECT:
                            {
                                serverResponsed = new ServerResponse(  "you are now logged in" , true );
                                os.writeObject(serverResponsed);
                                //
                                //             dos.writeUTF("you are now logged in")
                                inner:
                                while(true ) {
                                    Packet packet =(Packet) is.readObject();
                                    //System.out.println(packet.getCommand());
                                    switch (packet.getCommand())
                                    {

                                        case BALANCE:
                                            Account account=DBController.readAccount(packet.getAccount().getUser_id() );
                                            packet.setAccount(account);
                                            os.writeObject(packet);
                                            continue inner;
                                        case OPERATION:
                                            switch (packet.getTransaction().getOperation())
                                            {
                                                case DEPOSIT:
                                                    ServerResponse depositresponse=new ServerResponse();
                                                    if(Account.editBalance(packet.getTransaction())==errorType.SUCCESS)
                                                        depositresponse.setResponse(Integer.toString(packet.getTransaction().getValue())+" was deposited into your account successfully");
                                                    os.writeObject(depositresponse);
                                                    continue inner;
                                                case WITHDRAW:
                                                    ServerResponse withdrawresponse=new ServerResponse();
                                                    if(Account.editBalance(packet.getTransaction())==errorType.SUCCESS)
                                                        withdrawresponse.setResponse(Integer.toString(packet.getTransaction().getValue())+" was withdrawn from your account successfully");
                                                    else
                                                        withdrawresponse.setResponse("Your current balance is not enough to withdraw "+Integer.toString(packet.getTransaction().getValue())+" from your account");
                                                    os.writeObject(withdrawresponse);
                                                    continue  inner;
                                                case TRANSFERTOSAMEBANK:
                                                    // Server Response
                                                    ServerResponse transferSameBankResponse=new ServerResponse();
                                                    if(Account.editBalance(packet.getTransaction())==errorType.SUCCESS)
                                                        transferSameBankResponse.setResponse(Integer.toString(packet.getTransaction().getValue())+" was transfer to another account successfully");
                                                    else if(Account.editBalance(packet.getTransaction())==errorType.DESTINATIONNOTEXIST)
                                                    {
                                                        transferSameBankResponse.setResponse("ID not Exist");
                                                    }
                                                    else {
                                                     System.out.println(Account.editBalance(packet.getTransaction())+"");
                                                        transferSameBankResponse.setResponse("Your current balance is not enough to tranfer  " + Integer.toString(packet.getTransaction().getValue()) + " to another account");
                                                    }os.writeObject(transferSameBankResponse);
                                                    continue  inner;

                                            }


                                            continue inner;
                                        case VIEWHISTORY:
                                            // Recieve packet
                                            String History = DBController.viewHistory(recivedPacket.getAccount());
                                            //System.out.println("view History output : "+History);


                                            serverResponsed = new ServerResponse(  "Your Transaction History is : \n"+History, true );
                                            os.writeObject(serverResponsed);
                                            //          dos.writeUTF("congratulations you have successfully registered");
                                            continue inner;
                                        default:
                                            continue inner;







                                    }




                                    ////////////////////////////here/////////////////////////////////////
                                }

                            }
                            case WRONGPASSWORD:
                                serverResponsed = new ServerResponse
                                        (  "WRONG PASSWORD TRY TO LOG in AGAIN" , false );
                                os.writeObject(serverResponsed);
                                //               dos.writeUTF("WRONG PASSWORD TRY TO LOG AGAIN");
                                continue outer;
                        }


                        continue outer;
                    case LOGOUT:
                        break outer;

                }
            }
        }
        catch (Exception e)
        {
            System.out.println("Something went wrong ");
        }
        System.out.println("A Client just left ");
        //    System.out.println("client number " +n + "just left");
    }
}