import java.util.Date;
import java.util.List;
import java.util.Map;

public class ATM {
    private Date current;
    
    private int cash5;
    private int cash10;
    private int cash20;
    private int cash50;

    private Map<String, Customer> customerMap;
    private Map<String, Manager> managerMap;
    private List<Transaction> transactionList;

    public static void main(String args[]) {
        System.out.println("ATM starting ...");
    }
}
