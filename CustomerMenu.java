import java.util.List;

public class CustomerMenu extends Menu {
  public CustomerMenu(ATM atm) {
    super("Customer Menu", atm);
  }

  @Override
  protected List<Option> getOptions() {
    List<Option> options = super.getOptions();
    options.add(0, new Option("Apply for a account", this::ApplyForAccount));
    List<Account> accounts = ((Customer) atm.getCurrentUser()).getAccouts();
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
    List<Account> accounts = ((Customer) atm.getCurrentUser()).getAccouts();
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
}