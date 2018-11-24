package com.company;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
                        int id = register(recivedPacket.getAccount());
                        Transaction initialTransaction=new Transaction(id,id,recivedPacket.getAccount().getBalance(),Transaction.operation.DEPOSIT);
                        addToHistory(initialTransaction);
                        if(id!=-1)
                        {
                            serverResponsed = new ServerResponse(  "you are now Registered and your id is "
                                    + id , true );
                            os.writeObject(serverResponsed);

                        }
                        else
                        {
                            serverResponsed = new ServerResponse("registration failed", false);
                            os.writeObject(serverResponsed);
                        }
                        continue outer;
                    case LOGIN:

                        statusLogin sLI;
                        if(recivedPacket.getFromServer()==true)
                        {
                            sLI=statusLogin.CORRECT;
                        }
                        else
                            sLI= login(recivedPacket.getAccount());

                        switch (sLI)
                        {
                            case WRONGID:
                                serverResponsed = new ServerResponse(  "WRONG ID TRY TO LOG AGAIN" , false );
                                os.writeObject(serverResponsed);
                                continue outer;
                            case CORRECT:
                            {
                                serverResponsed = new ServerResponse(  "you are now logged in" , true );
                                os.writeObject(serverResponsed);

                                inner:
                                while(true ) {
                                    Packet packet =(Packet) is.readObject();

                                    switch (packet.getCommand())
                                    {

                                        case BALANCE:
                                            Account account= readAccount(packet.getAccount().getUser_id() );
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
                                                    Transaction innerWithdrawTransaction=new Transaction(sourceID,destinationID,value, Transaction.operation.WITHDRAW);
                                                    Transaction outerDebositTransaction=new Transaction(destinationID,sourceID,value, Transaction.operation.DEPOSIT);
                                                    if(readAccount(sourceID).getBalance()>=value)
                                                    {
                                                        Packet packetToSend=new Packet(new Account(),outerDebositTransaction, Packet.command.LOGIN,true);
                                                        outStream.writeObject(packetToSend);
                                                        responseFromAnotherBank=(ServerResponse)inStream.readObject();
                                                        if(responseFromAnotherBank.isSucces()==true)
                                                        {
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
                                                        }
                                                        else
                                                        {
                                                            transferToAnotherBankResponse.setResponse("Wrong Destination ID");
                                                        }

                                                    }
                                                    else
                                                    {
                                                        transferToAnotherBankResponse.setResponse("Your Balance is not enough to transfer "+Integer.toString(value));
                                                    }
                                                    os.writeObject(transferToAnotherBankResponse);
                                                    continue inner;
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
                                            String History = viewHistory(recivedPacket.getAccount());
                                            serverResponsed = new ServerResponse(  "Your Transaction History is : \n"+History, true );
                                            os.writeObject(serverResponsed);
                                            continue inner;


                                        default:
                                            continue inner;

                                    }
                                }

                            }
                            case WRONGPASSWORD:
                                serverResponsed = new ServerResponse
                                        (  "WRONG PASSWORD TRY TO LOG in AGAIN" , false );
                                os.writeObject(serverResponsed);
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
    }
}