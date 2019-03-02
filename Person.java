import java.util.regex.Pattern;

public abstract class Person {
    private String username;
    private String password;

    public Person(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    public boolean CheckPassword(String password) {
        return this.password.equals(password);
    }

    public static boolean IsValidName(String username) {
        Pattern pattern = Pattern.compile("^\\w+$");
        return pattern.matcher(username).matches();
    }

    public static boolean IsValidPassword(String password) {
        Pattern pattern = Pattern.compile("^\\w{6,}$");
        return pattern.matcher(password).matches();
    }
}
