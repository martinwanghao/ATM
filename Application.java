import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Application implements Saver {
  private String username;
  private AccountType type;

  public Application(String username, AccountType type) {
    this.username = username;
    this.type = type;
  }

  public String getUsername() {
    return this.username;
  }

  public String getKey() {
    return this.username + "," + this.type.getIndex();
  }

  public AccountType getAccountType() {
    return this.type;
  }

  public static Application Loader(Scanner in) {
    String username = in.nextLine();
    int typeindex = Integer.parseInt(in.nextLine());

    return new Application(username, AccountType.valueOf(typeindex));
  }

  @Override
  public void Write(PrintWriter out) throws IOException {
    out.write(this.username + "\n");
    out.write(this.type.getIndex() + "\n");
  }
}