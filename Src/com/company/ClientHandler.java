package com.company;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.Scanner;

import static com.company.DBController.*;


public class ClientHandler  implements Runnable
{
    Socket c;
    private static int serverNo;
    public static void setServerNo(int serverNo) {
        ClientHandler.serverNo = serverNo;
    }

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
                        //statusLogin sLI = DBController.login(recivedPacket.getAccount());
                        statusLogin sLI;
                        if(recivedPacket.getFromServer()==true)
                        {
                            sLI=statusLogin.CORRECT;
                        }
                        else
                            sLI=DBController.login(recivedPacket.getAccount());

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
                                                    {
                                                        depositresponse.setResponse(Integer.toString(packet.getTransaction().getValue())+" was deposited into your account successfully");
                                                        depositresponse.setSucces(true);
                                                    }
                                                    else
                                                    {
                                                        depositresponse.setResponse("Deposit did not succeeded");
                                                        depositresponse.setSucces(false);
                                                    }

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
                                                case TRANSFERTOANOTHERBANK:
                                                    File file=new File("Server.txt");
                                                    Scanner fileScanner=new Scanner(file);
                                                    int portNumber1=Integer.parseInt(fileScanner.nextLine());
                                                    String serverIp1=fileScanner.nextLine();
                                                    int portNumber2=Integer.parseInt(fileScanner.nextLine());
                                                    String serverIp2=fileScanner.nextLine();
                                                    int portNo;
                                                    String serverIP;
                                                    if(serverNo==1)
                                                    {
                                                        serverIP=serverIp2;
                                                        portNo=portNumber2;
                                                    }
                                                    else
                                                    {
                                                        serverIP=serverIp1;
                                                        portNo=portNumber1;
                                                    }
                                                    ServerResponse transferToAnotherBankResponse=new ServerResponse();
                                                    ServerResponse responseFromAnotherBank=new ServerResponse();
                                                    Socket socket=new Socket(serverIP,portNo);
                                                    ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
                                                    ObjectInputStream  inStream = new ObjectInputStream (socket.getInputStream()) ;

                                                    int sourceID=packet.getTransaction().getSource();
                                                    int destinationID=packet.getTransaction().getDestination();
                                                    int value=packet.getTransaction().getValue();
                                                    Transaction innerWithdrawTransaction=new Transaction(sourceID,sourceID,value, Transaction.operation.WITHDRAW);
                                                    Transaction outerDebositTransaction=new Transaction(destinationID,destinationID,value, Transaction.operation.DEPOSIT);
                                                    if(readAccount(sourceID).getBalance()>=value)
                                                    {
                                                        Packet packetToSend=new Packet(new Account(),outerDebositTransaction, Packet.command.LOGIN,true);
                                                        outStream.writeObject(packetToSend);
                                                        responseFromAnotherBank=(ServerResponse)inStream.readObject();
                                                        //System.out.println(responseFromAnotherBank.getResponse());
                                                        packetToSend = new Packet(new Account(),outerDebositTransaction, Packet.command.OPERATION,true);
                                                        outStream.writeObject(packetToSend);
                                                        responseFromAnotherBank=(ServerResponse)inStream.readObject();
                                                        if(responseFromAnotherBank.isSucces()==true)
                                                        {
                                                            Account.editBalance(innerWithdrawTransaction);
                                                            transferToAnotherBankResponse.setResponse("Transfer Complete");
                                                        }
                                                        else
                                                        {
                                                            transferToAnotherBankResponse.setResponse("Transfer Failed");
                                                        }

                                                        //System.out.println(responseFromAnotherBank.getResponse());

                                                    }
                                                    else
                                                    {
                                                        transferToAnotherBankResponse.setResponse("Your Balance is not enough to transfer "+Integer.toString(value));
                                                    }
                                                    os.writeObject(transferToAnotherBankResponse);
                                                    continue inner;

                                            }


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
                    case LOGOUT:
                        break outer;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println("Something went wrong "+e.getMessage());
        }
        System.out.println("A Client just left ");
        //    System.out.println("client number " +n + "just left");
    }
}