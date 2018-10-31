package com.company;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler  implements Runnable
{
    Socket c;
    public ClientHandler(Socket c)
    {
        this.c = c;
    }
    @Override
    public void run()
    {
        try
        {
            ObjectOutputStream os = new ObjectOutputStream(c.getOutputStream());
            ObjectInputStream  is = new ObjectInputStream(c.getInputStream());
            Account account;
            while (true)
            {
                account=(Account)is.readObject();
                System.out.println(account.getBalance());
            }
        }
        catch (Exception e)
        {
            System.out.println("Something went wrong ");
        }
    }
}





