public class Screen {
  private java.io.Console console = System.console();

  public String GetInput(String msg) {
    while (true) {
      String s = console.readLine(msg);
      if (!s.isEmpty())
        return s;
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

  public void ShowConfirmMsg(String msg) {
    ShowMsg(msg);
    this.GetInput("Press RETURN to continue", "");
  }

}