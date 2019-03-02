public class Console {
  private java.io.Console console = System.console();

  public String GetInput(String msg) {
    return console.readLine(msg);
  }

  public String GetPassword(String msg) {
    return new String(console.readPassword(msg));
  }

  public void Print(String format, Object ... args) {
    console.printf(format, args);
    console.flush();
  }

  public void Println(String format, Object ... args) {
    this.Print(format + "\n", args);
  }

  public void showError(String msg) {
    this.Println("\nERROR: " + msg);
    this.GetInput("Press RETURN to continue");
  }

  public void ShowInfo(String msg) {
    this.Println("\nINFO: " + msg);
    this.GetInput("Press RETURN to continue");
  }
}