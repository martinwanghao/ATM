import java.util.regex.Pattern;

public class Screen {
  private java.io.Console console = System.console();
  private static Pattern patternSelected = Pattern.compile("^\\d+$");

  public String GetInput(String msg) {
    while (true) {
      String s = console.readLine(msg);
      if (!s.isEmpty())
        return s;
    }
  }

  public int GetChoice(String msg, int min, int max) {
    while (true) {
      String s = GetInput(msg);
      if (!patternSelected.matcher(s).matches()) {
        ShowMsg("ERROR: invalid choice, you can only choose " + min + " to " + max);
        continue;
      }
      int selected = Integer.parseInt(s);
      if (selected < min || selected > max) {
        ShowMsg("ERROR: invalid choice, you can only choose " + min + " to " + max);
        continue;
      }
      return selected;
    }
  }

  public String GetInput(String msg, String defaultValue) {
    String s = console.readLine(msg);
    return s.isEmpty() ? defaultValue : s;
  }

  public boolean GetConfirm(String msg, String defaultValue) {
    String answer = GetInput(msg, defaultValue);
    return answer.toLowerCase().equals("y") || (answer.isEmpty() && defaultValue.toLowerCase().equals("y"));
  }

  public String GetPassword(String msg) {
    while (true) {
      String s = new String(console.readPassword(msg));
      if (!s.isEmpty())
        return s;
    }
  }

  public String GetPassword(String msg, String defaultValue) {
    String s = new String(console.readPassword(msg));
    return s.isEmpty() ? defaultValue : s;
  }

  public void ShowMsg(String msg) {
    console.printf(msg + "\n");
  }

  public void ShowPartMsg(String msg) {
    console.printf(msg);
    // try {
    //   for (int i = 0; i < 6; i++) {
    //     Thread.sleep(200);
    //     console.printf(".");
    //   }
    //   console.printf(" ");
    // } catch (InterruptedException e) {
    // }
  }

  public void ShowConfirmMsg(String msg) {
    ShowMsg(msg);
    this.GetInput("Press RETURN to continue", "");
  }

}