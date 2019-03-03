import java.util.regex.Pattern;

public abstract class User {
    private String username;
    private String password;

    public User(String username, String password) {
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

    private static Pattern patternName = Pattern.compile("^\\w+$");
    private static Pattern patternPassword = Pattern.compile("^\\w{6,}$");

    public static boolean IsValidName(String username) {
        return patternName.matcher(username).matches();
    }

    public static boolean IsValidPassword(String password) {
        return patternPassword.matcher(password).matches();
    }
}
