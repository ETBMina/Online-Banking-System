package com.company;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.*;

import static com.company.DBController.createConnection;
import static com.company.DBController.createStatement;


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
            Statement stmt = createStatement(createConnection());
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
                        boolean registered = DBController.register(recivedPacket.getAccount(),stmt);
                        if(registered==true)
                        {
                            serverResponsed = new ServerResponse(  "you are now Registered and your id is "
                                    + recivedPacket.getAccount().getUser_id() , true );
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
                        statusLogin sLI = DBController.login(recivedPacket.getAccount() ,createConnection() ,stmt);
                         switch (sLI)
                        {
                            case WRONGID:
                                // dos.writeUTF("WRONG ID TRY TO LOG AGAIN");
                                serverResponsed = new ServerResponse(  "WRONG ID TRY TO LOG AGAIN" , false );
                                os.writeObject(serverResponsed);
                                continue outer;
                            case CORRECT:
                            {
                                 serverResponsed = new ServerResponse(  "you are now loged in" , true );
                                 os.writeObject(serverResponsed);
                                //                 dos.writeUTF("you are now loged in");
                                 while(true ) {





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
              System.out.println("Something went wrong ");
        }
        System.out.println("A Client just left ");
        //    System.out.println("client number " +n + "just left");
    }
}