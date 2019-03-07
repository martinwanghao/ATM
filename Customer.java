import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
  private List<Account> accounts = new ArrayList<Account>();

  public Customer(String username, String password) {
    super(username, password);
  }

  public void AddAccount(Account a) {
    accounts.add(a);
  }

  public List<Account> getAccouts() {
    return accounts;
  }
}
