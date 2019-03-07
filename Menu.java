import java.util.ArrayList;
import java.util.List;

public abstract class Menu {
  protected class Option {
    private String title;
    private Runnable fun;

    public Option(String title, Runnable fun) {
      this.title = title;
      this.fun = fun;
    }
  }

  private String title;
  protected ATM atm;
  protected Screen screen;

  public Menu(String title, ATM atm) {
    this.title = title;
    this.atm = atm;
    this.screen = atm.getScreen();

  }

  // protected Menu AddOption(String title, Runnable fun) {
  // this.options.add(0, new Option(title, fun));
  // return this;
  // }

  public void Show() {
    List<Option> options = getOptions();
    String s = "";
    for (int i = 0; i < this.title.length(); i++)
      s += "═";
    screen.ShowMsg("\n" + this.title + "\n" + s);
    for (int i = 1; i <= options.size(); i++) {
      screen.ShowMsg(String.valueOf(i) + ". " + options.get(i - 1).title);
    }
    int selected = screen.GetChoice("\nPlease enter your choice: ", 1, options.size());
    Option option = options.get(selected - 1);
    if (option != null && option.fun != null)
      option.fun.run();
  }

  private void _ChangePassword() {
    User user = atm.getCurrentUser();
    String passwordOld = screen.GetPassword("Old password: ", "");
    if (!user.CheckPassword(passwordOld)) {
      screen.ShowConfirmMsg("ERROR: password is wrong");
      return;
    }
    String password = screen.GetPassword("New password: ");
    if (!User.IsValidPassword(password)) {
      screen.ShowConfirmMsg("ERROR: invalid password");
      return;
    }
    if (user.CheckPassword(password)) {
      screen.ShowConfirmMsg("ERROR: new password is the same with old password");
      return;
    }
    String password2 = screen.GetPassword("New password again: ", "");
    if (!password.equals(password2)) {
      screen.ShowConfirmMsg("ERROR: The two passwords you typed do not match");
      return;
    }
    user.setPassword(password);
    screen.ShowConfirmMsg("SUCCESS: Password changed");
  }

  protected List<Option> getOptions() {
    List<Option> options = new ArrayList<Option>();
    options.add(0, new Option("Logout", () -> atm.Logout()));
    options.add(0, new Option("Change Password", this::_ChangePassword));
    return options;
  }
}