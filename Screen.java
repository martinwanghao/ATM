public class Screen {
  private java.io.Console console = System.console();

  public String GetInput(String msg) {
    return console.readLine(msg);
  }

  public boolean GetConfirm(String msg, String defaultValue) {
    String answer = GetInput(msg);
    return answer.toLowerCase().equals("y") || (answer.isEmpty() && defaultValue.toLowerCase().equals("y"));
  }

  public String GetPassword(String msg) {
    return new String(console.readPassword(msg));
  }

  public void ShowMsg(String msg) {
    console.printf(msg + "\n");
  }

  public void ShowConfirmMsg(String msg) {
    ShowMsg(msg);
    this.GetInput("Press RETURN to continue");
  }

}