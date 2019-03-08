import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomerMenu extends Menu {

  private static final Pattern patternInput = Pattern.compile("\\s*\\$(?<den>(5|10|20|50))\\s*:\\s*(?<num>\\d+)");

  private Customer customer;
  private List<Account> accounts;

  public CustomerMenu(ATM atm) {
    super("Customer Menu", atm);
    this.customer = (Customer) atm.getCurrentUser();
    this.accounts = this.customer.getAccounts();
  }

  @Override
  protected List<Option> getOptions() {
    List<Option> options = super.getOptions();
    options.add(0, new Option("Apply for a account", this::ApplyForAccount));
    if (accounts.size() > 0) {
      options.add(0, new Option("Deposit", this::Deposit));
    }
    if (accounts.size() == 1)
      options.add(0, new Option("Show 1 account summary", this::ShowAccountSummary));
    else if (accounts.size() > 1)
      options.add(0, new Option("Show " + accounts.size() + " accounts summary", this::ShowAccountSummary));
    return options;
  }

  private void ApplyForAccount() {
    screen.ShowMsg("\nWhich kind of account do you want to apply for?");
    for (int i = 1; i <= AccountType.values().length; i++) {
      screen.ShowMsg(i + ". " + AccountType.values()[i - 1].getName());
    }
    screen.ShowMsg("0. Back to main menu");
    int selected = screen.GetChoice("\nPlease enter your choice: ", 0, AccountType.values().length);
    if (selected == 0)
      return;
    try {
      atm.ApplyForAccount(AccountType.valueOf(selected));
    } catch (Exception e) {
      screen.ShowConfirmMsg("FAILED: " + e.getMessage());
      return;
    }
    screen.ShowConfirmMsg("SUCCESS: your application is sended");
    return;
  }

  private void ShowAccountSummary() {
    if (accounts.size() == 1)
      screen.ShowMsg("\nYou have only 1 account:");
    else
      screen.ShowMsg("\nYou have " + accounts.size() + " accounts:");
    for (int i = 1; i <= accounts.size(); i++) {
      Account a = accounts.get(i - 1);
      screen.ShowMsg("[" + a.getNum() + "] " + AccountType.valueOf(a).getName() + ", "
          + ATM.toShortDateString(a.getCreationTime()) + ", Balance = " + a.getBalance());
    }
    screen.ShowConfirmMsg("");
    return;
  }

  private void Deposit() {
    screen.ShowMsg("\nPlease select a acount to deposit:");
    for (int i = 1; i <= accounts.size(); i++) {
      Account a = accounts.get(i - 1);
      screen
          .ShowMsg(i + ". [" + a.getNum() + "] " + AccountType.valueOf(a).getName() + ", Balance = " + a.getBalance());
    }
    screen.ShowMsg("0. Back to main menu");
    int selectedAccount = screen.GetChoice("\nPlease enter your choice: ", 0, AccountType.values().length);
    if (selectedAccount == 0)
      return;

    int amount = 0;
    while (true) {
      String input = screen.GetInput("How many money do you want to deposit(e.g. $5:10, $10:3, $20:5, $50:1): ", "");
      if (input.isEmpty()) {
        screen.ShowConfirmMsg("CANCELED: You do not input any money");
        return;
      }
      Matcher m = patternInput.matcher(input);
      while (m.find()) {
        amount += Integer.parseInt(m.group("den")) * Integer.parseInt(m.group("num"));
      }
      if (amount > 0)
        break;
      screen.ShowConfirmMsg("ERROR: I can't recognize your input");
    }
    accounts.get(selectedAccount - 1).addBalance(amount);
    screen.ShowConfirmMsg("SUCCESS: You deposit $" + amount);
    return;
  }
}