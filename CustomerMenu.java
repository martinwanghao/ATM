import java.util.List;

public class CustomerMenu extends Menu {
  public CustomerMenu(ATM atm) {
    super("Customer Menu", atm);
  }

  @Override
  protected List<Option> getOptions() {
    List<Option> options = super.getOptions();
    options.add(0, new Option("Apply for a account", this::ApplyForAccount));
    return options;
  }

  private void ApplyForAccount() {
    screen.ShowMsg("\nWhich kind of account do you want to apply for?");
    for (int i = 1; i <= AccountType.values().length; i++) {
      screen.ShowMsg(i + ". " + AccountType.values()[i - 1].getName());
    }
    int selected = screen.GetChoice("\nPlease enter your choice: ", 1, AccountType.values().length);
    try {
      atm.ApplyForAccount(AccountType.valueOf(selected));
    } catch (Exception e) {
      screen.ShowConfirmMsg("FAILED: " + e.getMessage());
      return;
    }
    screen.ShowConfirmMsg("SUCCESS: your application is sended");
    return;
  }
}