public class ManagerMenu extends Menu {
  public ManagerMenu(ATM atm) {
    super("Manager Menu", atm);
    this.AddOption("Shutdown this ATM", this::_Shutdown);
    this.AddOption("Register Customer", this::_RegisterCustomer);
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