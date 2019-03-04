import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.regex.Pattern;

public abstract class User implements Save {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public boolean CheckPassword(String password) {
        return this.password.equals(password);
    }

    private static Pattern patternName = Pattern.compile("^\\w+$");
    private static Pattern patternPassword = Pattern.compile("^\\w{6,}$");

    public static boolean IsValidName(String username) {
        return patternName.matcher(username).matches();
    }

    public static boolean IsValidPassword(String password) {
        return patternPassword.matcher(password).matches();
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static User Loader(Scanner in) {
        try {
            String usertype = in.nextLine();
            String username = in.nextLine();
            String password = in.nextLine();

            if (usertype.equals("Manager")) {
                return new Manager(username, password);
            } else if (usertype.equals("Customer")) {
                return new Customer(username, password);
            }
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public void Write(PrintWriter out) throws IOException {
        out.write(this.getClass().getName() + "\n");
        out.write(this.username + "\n");
        out.write(this.password + "\n");
    }
}
