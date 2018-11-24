package com.company;


import java.io.*;
import java.net.Socket;
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
            File file=new File("Server.txt");
            Scanner fileScanner=new Scanner(file);
            String portNumber1=fileScanner.nextLine();
            String serverIp1=fileScanner.nextLine();
            String portNumber2=fileScanner.nextLine();
            String serverIp2=fileScanner.nextLine();
            int serverNo;
            Socket c;
            label:
            while(true)
            {
                System.out.println("Enter 1 to connect to the first server or 2 to connect to the second:");

                serverNo=sc.nextInt();
                if(serverNo==1)
                {
                    c = new Socket(serverIp1, Integer.parseInt(portNumber1));
                    break ;
                }
                else if(serverNo==2)
                {
                    c = new Socket(serverIp2, Integer.parseInt(portNumber2));
                    break;
                }
                else
                {
                    System.out.println("Wrong Choice");
                    continue label;
                }
            }
            sc.nextLine();
            //open comm
            ObjectOutputStream os = new ObjectOutputStream(c.getOutputStream());
            ObjectInputStream  is = new ObjectInputStream (c.getInputStream()) ;
            //perform I/O
            Packet packetToSend = new Packet();
            ServerResponse packetToRecive = new ServerResponse();
            //the program itself
            System.out.println("Welcome to our Client Payment Application");
            outer :
            while (true)
            {

                //welcome user
                System.out.println("please, enter 1 to login");
                System.out.println("        enter 2 to register");
                System.out.println("        enter 3 to close the application");
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
                        //System.out.println("Enter your PASSWORD :");
                        String password="";

                        Console console = System.console();
                        char[] text = console.readPassword("Enter password : ");
                        password=String.valueOf(text);
                        //password = sc.nextLine();

                        if(!checkUserPassword(password)) {
                            System.out.println("INVALID PASSWORD .");
                            continue outer;
                        }

                        //sending to server
                        System.out.println("please hold while we contact the server.");
                        Account account=new Account(Integer.parseInt(userId),Integer.parseInt(password));
                        packetToSend =
                                new Packet( account , Packet.command.LOGIN  );
                        os.writeObject(packetToSend);

                        //reciving packet response
                        packetToRecive =(ServerResponse) is.readObject();
                        System.out.println(packetToRecive.getResponse());
                        if(packetToRecive.isSucces())
                            inner:
                            while(true)
                            {
                                System.out.println("please, enter 1 to check your current balance"     +"\n"+
                                                   "        enter 2 to deposit cash to your account"   +"\n"+
                                                   "        enter 3 to withdraw cash from your account"+"\n"+
                                                   "        enter 4 to transfer Money to another account within our bank"+"\n"+
                                                   "        enter 5 to transfer money to another account in another bank"+"\n"+
                                                   "        enter 6 to view History"                   +"\n"+
                                                   "        enter 7 to logout"                   +"\n");

                                String userInputChoice = sc.nextLine();
                                Packet receivedPacket=new Packet();
                                switch (userInputChoice)
                                {
                                    case "1":
                                        Packet p=new Packet(packetToSend.getAccount(),Packet.command.BALANCE);
                                        System.out.println("please hold while we contact the server.");
                                        os.writeObject(p);
                                        receivedPacket =(Packet) is.readObject();
                                        System.out.println("Your current balance = "+receivedPacket.getAccount().getBalance());


                                        continue inner;
                                    case "2":
                                        System.out.println("please enter the needed amount of cash to deposit in your accunt: ");
                                        String value = sc.nextLine();
                                        if(!isStringInteger(value)){
                                            System.out.println("Invalid amount of cash");
                                        }
                                        else if(Integer.parseInt(value)<=0){
                                            System.out.println("Invalid amount of cash");
                                        }
                                        else{
                                            Transaction trans=new Transaction(account.getUser_id(),account.getUser_id(),Integer.parseInt(value),Transaction.operation.DEPOSIT);
                                            Packet DepositPacket=new Packet(packetToSend.getAccount(),trans,Packet.command.OPERATION);
                                            System.out.println("please hold while we contact the server.");
                                            os.writeObject(DepositPacket);
                                            packetToRecive =(ServerResponse) is.readObject();
                                            System.out.println(packetToRecive.getResponse());
                                        }
                                        continue inner;
                                    case "3":
                                        System.out.println("please enter the needed amount of cash to withdraw from your accunt: ");
                                        value = sc.nextLine();
                                        if(!isStringInteger(value)){
                                            System.out.println("Invalid amount of cash");
                                        }
                                        else if(Integer.parseInt(value)<=0){
                                            System.out.println("Invalid amount of cash");
                                        }
                                        else{
                                            Transaction trans=new Transaction(account.getUser_id(),account.getUser_id(),Integer.parseInt(value),Transaction.operation.WITHDRAW);
                                            Packet DepositPacket=new Packet(packetToSend.getAccount(),trans,Packet.command.OPERATION);
                                            System.out.println("please hold while we contact the server.");
                                            os.writeObject(DepositPacket);
                                            packetToRecive =(ServerResponse) is.readObject();
                                            System.out.println(packetToRecive.getResponse());
                                        }
                                        continue inner;
                                    case "4":
                                        // Another account
                                        Account anotherAccount = new Account() ;
                                        // ID of another account
                                        String anotherValue;
                                        // get amount of cash to transfer
                                        System.out.println("please enter the amount of cash to Transfer to another accunt: ");
                                        value = sc.nextLine();
                                        // Validate amount of money
                                        if(!isStringInteger(value)||Integer.parseInt(value)<=0){
                                            System.out.println("Invalid amount of cash");
                                        }
                                        // get id of another account
                                        else {
                                            System.out.println("please enter the id of another accunt: ");
                                            anotherValue = sc.nextLine();
                                            // validate another account id
                                            if (!isStringInteger(anotherValue) || Integer.parseInt(anotherValue) <= 0)
                                                System.out.println("Invalid Id");
                                            else if (Integer.parseInt(anotherValue) == account.getUser_id())
                                                System.out.println("you can't transfer to your account");
                                            else {
                                                anotherAccount.setUser_id(Integer.parseInt(anotherValue));
                                                Transaction trans = new Transaction(account.getUser_id(), anotherAccount.getUser_id(), Integer.parseInt(value), Transaction.operation.TRANSFERTOSAMEBANK);
                                                Packet DepositPacket = new Packet(packetToSend.getAccount(), trans, Packet.command.OPERATION);
                                                System.out.println("please hold while we contact the server.");
                                                os.writeObject(DepositPacket);
                                                packetToRecive = (ServerResponse) is.readObject();
                                                System.out.println(packetToRecive.getResponse());
                                            }
                                        }
                                        continue inner;
                                    case "5":
                                        System.out.println("please enter the amount of money to transfer:");
                                        value=sc.nextLine();
                                        if(!isStringInteger(value)){
                                            System.out.println("Invalid amount of cash");
                                        }
                                        else if(Integer.parseInt(value)<=0){
                                            System.out.println("Invalid amount of cash");
                                        }
                                        else
                                        {
                                            System.out.println("please enter the user id to transfer money to him:");
                                            String destinationID=sc.nextLine();
                                            if(!isStringInteger(destinationID)){
                                                System.out.println("Invalid ID");
                                            }
                                            else if(Integer.parseInt(destinationID)<=0){
                                                System.out.println("Invalid ID");
                                            }
                                            else
                                            {
                                                Transaction trans=new Transaction(account.getUser_id(),Integer.parseInt(destinationID),Integer.parseInt(value), Transaction.operation.TRANSFERTOANOTHERBANK);
                                                Packet transferToAnotherBankPacket=new Packet(packetToSend.getAccount(),trans, Packet.command.OPERATION);
                                                System.out.println("please hold while we contact the server.");
                                                os.writeObject(transferToAnotherBankPacket);
                                                packetToRecive =(ServerResponse) is.readObject();
                                                System.out.println(packetToRecive.getResponse());
                                            }

                                        }
                                        break;
                                    case "6":
                                        //sending packet
                                        Packet viewHistoryPacket=new Packet(packetToSend.getAccount(),Packet.command.VIEWHISTORY);
                                        System.out.println("please hold while we contact the server.");
                                        os.writeObject(viewHistoryPacket);
                                        packetToRecive =(ServerResponse) is.readObject();
                                        System.out.println(packetToRecive.getResponse());
                                        continue inner;
                                    case "7":
                                        packetToSend = new Packet(Packet.command.LOGOUT);
                                        os.writeObject(packetToSend);
                                        System.out.println("come visit us again soon BYE. ");
                                        break outer;
                                    default:
                                        System.out.println("error in your option try again ");
                                        break;


                                }


                            }

                        continue outer;
                    case "2" :
                        //taking in full name
                        System.out.println("Enter your FULLNAME : ");
                        String fullName = sc.nextLine();

                        //taking in password
                        String pass = "";
                        do {
                            console = System.console();
                            text = console.readPassword("Enter your PASSWORD(Must consist of 4 digits only):  ");
                            pass=String.valueOf(text);
                            if (!checkUserPassword(pass))
                                System.out.println("INVALID PASSWORD  try  again");
                        }while(!checkUserPassword(pass));

                        //taking in balance
                        String balance ="" ;
                        do {
                            System.out.println("Enter an initial amount to deposit : ");
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
            os.close();
            is.close();
            c.close();
        }
        catch (IOException ex)
        {
              System.out.println("config file does not exist please confirm with our support team");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
    public static boolean isStringInteger(String number ){
        try{
            Integer.parseInt(number);
        }catch(Exception e ){
            return false;
        }
        return true;
    }
}
