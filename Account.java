import java.util.Date;
import java.util.List;

public abstract class Account {
    private double balance;
    private Date creationDate;
    private Manager creator;
    private Customer customer;
    private List<Transaction> transactionList;
    private String num;
}
