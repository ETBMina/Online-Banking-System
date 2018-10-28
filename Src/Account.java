public class Account {

    private int user_id;
    private int password;
    private int balance ;
    private  String full_name ;

    public Account(int user_id, int password, int balance, String full_name) {
        this.user_id = user_id;
        this.password = password;
        this.balance = balance;
        this.full_name = full_name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }


    public boolean edit (Account source , int value )
    {
        if (source==this)
        {
            if (value>0)
        {
            balance+=value ;
            return true;
        }
        else
        {

            if(balance>(value*-1))
            {
                balance -= (value*-1);
                return true;
            }
            else
                return false;
        }
        }
        else
            {
                if (value>0)
                {
                    if (source.balance>value)
                    {
                        source.balance-=value;
                        this.balance+=value;
                        return true;
                    }
                    else
                        return false;

                }
                else
                if (this.balance>(value*-1))
                {
                    this.balance-=value;
                    source.balance+=value;
                    return true;
                }
                else
                    return false;

            }

    }
}



