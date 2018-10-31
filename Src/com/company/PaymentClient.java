package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class PaymentClient
{

    public static void main(String[] args)
    {
        try
        {
            Account account=new Account("Ahmed",123,125);
            Scanner sc = new Scanner(System.in);
            //1.connect
            //2. create socket
            Socket c = new Socket("127.0.0.1", 1234);
            ObjectOutputStream os=new ObjectOutputStream(c.getOutputStream());
           /* DataOutputStream dos = new DataOutputStream(c.getOutputStream());
            DataInputStream dis = new DataInputStream(c.getInputStream());*/

            //3. perform IO
            while (true)
            {
                //String servermsg = dis.readUTF();
                //System.out.println(servermsg);
                String userresp = sc.nextLine();
                os.writeObject(account);
                if(userresp.equalsIgnoreCase("N"))
                    break;
            }

            //4.close comm
            os.close();
           // dis.close();
            c.close();
        } catch (IOException ex)
        {

        }
    }

}
