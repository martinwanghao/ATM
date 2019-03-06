public class CustomerMenu extends Menu {
  public CustomerMenu(ATM atm) {
    super("Customer Menu", atm);
    this.AddOption("Apply for a account", this::ApplyForAccount);
  }

  private void ApplyForAccount() {
    screen.ShowMsg("Which kind of account do you want to apply for?");
    for (int i = 1; i <= AccountType.values().length; i++) {
      screen.ShowMsg(i + ". " + AccountType.values()[i - 1]);
    }
    int selected = screen.GetChoice("\nPlease enter your choice: ", 1, AccountType.values().length);
    try {
      atm.ApplyForAccount(AccountType.valueOf(selected));
    } catch (Exception e) {
      screen.ShowConfirmMsg("FAILED: " + e.getMessage());
    }
    screen.ShowConfirmMsg("SUCCESS: your application is sended");
    return;
  }
}