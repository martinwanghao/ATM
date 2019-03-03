import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ATM {
    private Date initTime;
    private Date initClock;
    private boolean powerON = true;
    private Screen screen = new Screen();

    private int numOfCash5 = 100;
    private int numOfCash10 = 100;
    private int numOfCash20 = 100;
    private int numOfCash50 = 100;

    private Map<String, User> users = new HashMap<String, User>();
    private List<Transaction> transactionList = new ArrayList<Transaction>();

    private User currentUser = null;
    private static Pattern patternTime = Pattern
            .compile("^(?<year>\\d{4})-(?<month>\\d{1,2})-(?<day>\\d{1,2})\\s+(?<hour>\\d{1,2}):(?<minute>\\d{1,2})$");

    public ATM() {
    }

    private Menu managerMenu;

    /**
     * @return the managerMenu
     */
    public Menu getManagerMenu() {
        if (managerMenu == null) {
            managerMenu = new Menu("Manager Menu");
            managerMenu.AddOption("Shutdown this ATM", () -> {
                // ...
            });
            managerMenu.AddOption("Create account", () -> {
                // ...
            });
        }
        return managerMenu;
    }

    private Date getCurrentTime() {
        return new Date((new Date()).getTime() - initClock.getTime() + initTime.getTime());
    }

    private void Run() {
        screen.ShowMsg("\n\nATM is starting ...");
        this.Init();
        while (powerON)
            this.Login();
        screen.ShowMsg("ATM shutdown ...");
    }

    private String now() {
        SimpleDateFormat ft = new SimpleDateFormat("E yyyy-MM-dd 'at' hh:mm:ss a zzz");
        return ft.format(getCurrentTime());
    }

    private void Init() {
        screen.ShowMsg("ATM is Initializing ...");
        while (this.users.size() == 0) {
            screen.ShowMsg("\nNOTICE: this is a new ATM, you should do some initial things first.");
            while (!InitSetClock())
                ;
            while (!InitSetManagerAccount())
                ;
        }
    }

    private boolean InitSetClock() {
        String strTime = screen.GetInput("\nPlease input currect time(e.g. 2019-3-3 15:11): ");
        if (strTime.isEmpty()) {
            initTime = new Date();
            initClock = initTime;
        } else {
            Matcher m = patternTime.matcher(strTime);
            if (!m.matches()) {
                screen.ShowMsg("ERROR: time format is incorrect.");
                return false;
            }
            int year = Integer.parseInt(m.group("year"));
            int month = Integer.parseInt(m.group("month"));
            int day = Integer.parseInt(m.group("day"));
            int hour = Integer.parseInt(m.group("hour"));
            int minute = Integer.parseInt(m.group("minute"));
            if (year < 1900 || year > 2050 || month < 1 || month > 12 || day < 1 || day > 31 || hour < 0 || hour > 23
                    || minute < 0 || minute > 59) {
                screen.ShowMsg("ERROR: time format is incorrect.");
                return false;
            }
            Calendar time = new GregorianCalendar(year, month - 1, day, hour, minute);
            initTime = time.getTime();
            initClock = new Date();
        }

        screen.ShowMsg("" + now());
        return screen.GetConfirm("Is this time correct? (Y/n) : ", "y");
    }

    private boolean InitSetManagerAccount() {
        String username = screen.GetInput("\nPlease set manager's username: ");
        if (!User.IsValidName(username)) {
            screen.ShowMsg("ERROR: invalid username");
            return false;
        }
        String password = screen.GetPassword("Please input password: ");
        if (!User.IsValidPassword(password)) {
            screen.ShowMsg("ERROR: invalid password");
            return false;
        }
        String password2 = screen.GetPassword("Please input password again: ");
        if (!password.equals(password2)) {
            screen.ShowMsg("ERROR: not the same password");
            return false;
        }
        users.put(username, new Manager(username, password));
        screen.ShowMsg("SUCCESS: Manager " + username + " added");
        return true;
    }

    private void Login() {
        screen.ShowMsg("\n\nWelcome to use ATM!");
        screen.ShowMsg("" + now() + "\n");
        String userName = screen.GetInput("Username : ");
        String password = screen.GetPassword("Password : ");
        User person = this.users.get(userName);
        if (person == null || !person.CheckPassword(password)) {
            screen.ShowConfirmMsg("ERROR: username or password is incorrect");
            return;
        }
        if (person instanceof Manager) {
            this.getManagerMenu().Run();
        } else if (person instanceof Customer) {
            this.getManagerMenu().Run();
        }
    }

    public static void main(String args[]) {
        (new ATM()).Run();
    }
}
