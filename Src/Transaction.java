public class Transaction {

    private int source ;
    private int destination ;
    private int value ;

    public Transaction(int source, int destantion, int value) {
        this.source = source;
        this.destination = destantion;
        this.value = value;
    }


    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getDestantion() {
        return destination;
    }

    public void setDestantion(int destantion) {
        this.destination = destantion;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
    public String toString ()
    {
        String Print_Stirng = "";
        if (source==destination)
            if (value>0)
                Print_Stirng =" "+ Integer.toString(value) + " was  Deposited into Account NO = " + Integer.toString(source)  + " ";
            else
                Print_Stirng =" "+ Integer.toString((value*-1)) + " was  Withdrawn from Account NO = " + Integer.toString(source)  + " ";
        else
        if (value>0)
            Print_Stirng =" "+ Integer.toString(value) + " was  Deposited into Account NO = " + Integer.toString(destination)
                    + " from account No = "    +Integer.toString(source)+" "
                    ;
        else
            Print_Stirng =" "+ Integer.toString((value*-1)) + " was  Deposited into Account NO = " + Integer.toString(source)
                    + " from account No = "    +Integer.toString(destination)+" ";

        return  Print_Stirng;


    }


}
