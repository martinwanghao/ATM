import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Scanner;

public abstract class Account implements Saver {
  private String num;
  private String username;
  private float balance;
  Date creationtime;

  public String getNum() {
    return num;
  }

  public float getBalance() {
    return balance;
  }

  public void addBalance(float amount) {
    this.balance += amount;
  }

  public String getUsername() {
    return username;
  }

  public Date getCreationTime() {
    return creationtime;
  }

  protected Account(String username, String num, Date time) {
    this.num = num;
    this.username = username;
    this.balance = 0;
    this.creationtime = time;
  }

  public static Account Loader(Scanner in) {
    String type = in.nextLine();
    String username = in.nextLine();
    String num = in.nextLine();
    String balance = in.nextLine();
    String creationtime = in.nextLine();

    Account a = AccountType.valueOf(type).CreateAccount(username, num, new Date(Long.parseLong(creationtime)));
    a.balance = Float.parseFloat(balance);
    return a;
  }

  @Override
  public void Write(PrintWriter out) throws IOException {
    out.write(this.getClass().getName() + "\n");
    out.write(this.username + "\n");
    out.write(this.num + "\n");
    out.write(this.balance + "\n");
    out.write(this.creationtime.getTime() + "\n");
  }

}