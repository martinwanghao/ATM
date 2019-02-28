import java.util.Date;

public abstract class Account {
    private double balance;
    Date date_of_creation;
    Transaction[] transactions;
    String account_num;
    public Account(String num) {
        account_num = num;
        balance = 0;
    }
    public double getBalance(){
        return balance;
    }

}
