import java.util.List;

public class Customer extends Person {
  private List<Account> accountList;
  public Customer(String username, String password) {
    super(username, password);
  }
}
