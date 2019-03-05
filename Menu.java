import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Menu {
  private class Option {
    private String title;
    private Runnable fun;

    public Option(String title, Runnable fun) {
      this.title = title;
      this.fun = fun;
    }
  }

  private static Pattern patternSelected = Pattern.compile("^\\d+$");

  private String title;
  protected ATM atm;
  protected Screen screen;
  private List<Option> options = new ArrayList<Option>();

  public Menu(String title, ATM atm) {
    this.title = title;
    this.atm = atm;
    this.screen = atm.getScreen();
    this.AddOption("Logout", () -> atm.Logout());
    this.AddOption("Change Password", this::_ChangePassword);
  }

  protected Menu AddOption(String title, Runnable fun) {
    this.options.add(0, new Option(title, fun));
    return this;
  }

  private int GetSelected() {
    while (true) {
      String input = screen.GetInput("\nPlease choose : ", "");
      if (input.isEmpty())
        continue;
      if (!patternSelected.matcher(input).matches()) {
        screen.ShowMsg("ERROR: invalid option");
        continue;
      }
      int selected = Integer.parseInt(input);
      if (selected < 1 || selected > options.size()) {
        screen.ShowMsg("ERROR: invalid option");
        continue;
      }
      return selected;
    }
  }

  public void Show() {
    String s = "";
    for (int i = 0; i < this.title.length(); i++)
      s += "═";
    screen.ShowMsg("\n" + this.title + "\n" + s);
    for (int i = 1; i <= options.size(); i++) {
      screen.ShowMsg(String.valueOf(i) + ". " + options.get(i - 1).title);
    }
    int selected = GetSelected();
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
}