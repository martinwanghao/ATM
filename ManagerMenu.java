import java.util.List;

public class ManagerMenu extends Menu {
  public ManagerMenu(ATM atm) {
    super("Manager Menu", atm);
  }

  @Override
  protected List<Option> getOptions() {
    List<Option> options = super.getOptions();
    options.add(0, new Option("Shutdown this ATM", this::_Shutdown));
    List<Application> applications = atm.getApplications();
    if (applications.size() > 0)
      options.add(0,
          new Option("Check " + applications.size() + " open account application(s)", this::_CheckOpenAccount));

    options.add(0, new Option("Register Customer", this::_RegisterCustomer));
    return options;
  }

  private void _CheckOpenAccount() {
    List<Application> applications = atm.getApplications();
    while (applications.size() > 0) {
      screen.ShowMsg("\nWhich application do you want to check?");
      for (int i = 1; i <= applications.size(); i++) {
        Application a = applications.get(i - 1);
        screen.ShowMsg(i + ". " + a.getAccountType().getName() + ", " + a.getUsername());
      }
      screen.ShowMsg("0. Back to main menu");
      int selected = screen.GetChoice("\nPlease enter your choice: ", 0, applications.size());
      if (selected == 0)
        break;
      Application a = applications.get(selected - 1);
      try {
        atm.AddAccount(a);
      } catch (Exception e) {
        screen.ShowConfirmMsg("FAILED: " + e.getMessage());
        return;
      }
      screen.ShowMsg("SUCCESS: Open a " + a.getAccountType().getName() + " for " + a.getUsername());
      applications = atm.getApplications();
    }
  }

  private void _RegisterCustomer() {
    String username = screen.GetInput("\nPlease input the customer's username: ");
    if (!User.IsValidName(username)) {
      screen.ShowConfirmMsg("ERROR: invalid username");
      return;
    }

    String password = String.valueOf(100000 + (int) (Math.random() * 100000));
    try {
      atm.AddUser(new Customer(username, password));
    } catch (Exception e) {
      screen.ShowConfirmMsg("ERROR: " + e.getMessage());
      return;
    }
    screen.ShowConfirmMsg("SUCCESS: Customer account '" + username + "' added, password is '" + password + "'");
    return;
  }

  private void _Shutdown() {
    if (screen.GetConfirm("NOTICE: DO YOU REALLY WANT TO SHUTDOWN THIS ATM? (y/N) : ", "n"))
      atm.Shutdown();
  }
}