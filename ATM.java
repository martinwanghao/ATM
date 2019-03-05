import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ATM implements Saver {
    private GregorianCalendar initTime;
    private GregorianCalendar initClock;
    private boolean powerON;
    private Screen screen = new Screen();
    private Disk disk = new Disk();
    private User currentUser = null;
    private Map<String, User> users = new HashMap<String, User>();

    // private int numOfCash5 = 100;
    // private int numOfCash10 = 100;
    // private int numOfCash20 = 100;
    // private int numOfCash50 = 100;
    // private List<Transaction> transactionList = new ArrayList<Transaction>();

    private static Pattern patternTime = Pattern
            .compile("^(?<year>\\d{4})-(?<month>\\d{1,2})-(?<day>\\d{1,2})\\s+(?<hour>\\d{1,2}):(?<minute>\\d{1,2})$");

    private ManagerMenu managerMenu = new ManagerMenu(this);
    private CustomerMenu customerMenu = new CustomerMenu(this);

    public Screen getScreen() {
        return this.screen;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public void AddUser(User user) throws Exception {
        if (users.containsKey(user.getUsername()))
            throw new Exception("user already existed");
        users.put(user.getUsername(), user);
    }

    public void Logout() {
        this.currentUser = null;
    }

    public void Shutdown() {
        this.Logout();
        this.powerON = false;
    }

    private GregorianCalendar getCurrentTime() {
        GregorianCalendar now = new GregorianCalendar();
        now.setTime(new Date((new Date()).getTime() - initClock.getTime().getTime() + initTime.getTime().getTime()));
        return now;
    }

    private String getCurrentTimeString() {
        SimpleDateFormat ft = new SimpleDateFormat("E yyyy-MM-dd 'at' hh:mm:ss a zzz");
        return ft.format(getCurrentTime().getTime());
    }

    private void Run() {
        screen.ShowMsg("\n\n");
        screen.ShowMsg("╔═════════════════════════════════╗");
        screen.ShowMsg("║ :                             : ║");
        screen.ShowMsg("║                                 ║");
        screen.ShowMsg("║           Welcome to            ║");
        screen.ShowMsg("║         FLORA ATM v2.20         ║");
        screen.ShowMsg("║                                 ║");
        screen.ShowMsg("║ :                             : ║");
        screen.ShowMsg("╚═════════════════════════════════╝");
        screen.ShowMsg("");
        powerON = true;
        screen.ShowMsg("ATM powered ON");

        this.Load();

        while (powerON)
            this.Login();

        this.Save();
        screen.ShowMsg("\n\nATM powered OFF\n");
    }

    private void Load() {
        this.disk.LoadObject("./DISK/ATM.txt", this::Loader);
        if (this.initTime == null) {
            screen.ShowMsg("Loading ATM's time ... failed");
            screen.ShowMsg("\nNOTICE: You should set ATM's time first");
            while (!InitSetClock())
                ;
        } else {
            screen.ShowMsg("Loading ATM's time ... ok");
        }

        try {
            ArrayList<User> users = this.disk.LoadList("./DISK/Users.txt", User::Loader);
            users.forEach(o -> {
                this.users.put(o.getUsername(), o);
            });
            screen.ShowMsg("Loading user accounts ... " + this.users.size() + " account(s) loaded");
        } catch (IOException e) {
            screen.ShowMsg("Loading user accounts ... failed(" + e.getMessage() + ")");
        }
        if (this.users.size() == 0) {
            screen.ShowMsg("\nNOTICE: You should set at least one manager account");
            while (!InitSetManagerAccount())
                ;
        }
    }

    private boolean InitSetClock() {
        String strTime = screen.GetInput("Please input currect time(default: 2019-3-3 15:11): ", "2019-3-3 15:11");
        initClock = new GregorianCalendar();
        if (strTime.isEmpty()) {
            initTime = initClock;
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
            initTime = new GregorianCalendar(year, month - 1, day, hour, minute);
        }

        screen.ShowMsg(getCurrentTimeString());
        return screen.GetConfirm("Is this time correct? (Y/n) : ", "y");
    }

    private boolean InitSetManagerAccount() {
        String username = screen.GetInput("Please set manager's username(default: flora): ", "flora");
        if (!User.IsValidName(username)) {
            screen.ShowMsg("ERROR: invalid username");
            return false;
        }
        String password = screen.GetPassword("Please input password(default: loveyou): ", "loveyou");
        if (!User.IsValidPassword(password)) {
            screen.ShowMsg("ERROR: invalid password");
            return false;
        }
        String password2 = screen.GetPassword("Please input password again(default: loveyou): ", "loveyou");
        if (!password.equals(password2)) {
            screen.ShowMsg("ERROR: not the same password");
            return false;
        }
        users.put(username, new Manager(username, password));
        screen.ShowMsg("SUCCESS: Manager " + username + " added");
        return true;
    }

    public void Save() {
        File f = new File("./DISK");
        f.mkdir();
        try {
            disk.Save("./DISK/ATM.txt", this);
            screen.ShowMsg("Saving ATM's time ... ok");
        } catch (IOException e) {
            screen.ShowMsg("Saving ATM's time ... failed(" + e.getMessage() + ")");
        }

        try {
            disk.Save("./DISK/Users.txt", new ArrayList<Saver>(this.users.values()));
            screen.ShowMsg("Saving user acounts ... ok, " + this.users.size() + " account(s) saved");
        } catch (IOException e) {
            screen.ShowMsg("Saving user acounts ... failed(" + e.getMessage() + ")");
        }
    }

    private void Login() {
        screen.ShowMsg("\n\nWelcome to FLORA ATM");
        screen.ShowMsg(getCurrentTimeString() + "\n");
        String userName = screen.GetInput("Username : ", "");
        if (userName.isEmpty())
            return;
        String password = screen.GetPassword("Password : ", "");
        User user = this.users.get(userName);
        if (user == null || !user.CheckPassword(password)) {
            screen.ShowConfirmMsg("ERROR: username or password is incorrect");
            return;
        }
        this.currentUser = user;
        Menu menu = null;
        if (user instanceof Manager) {
            menu = managerMenu;
        } else if (user instanceof Customer) {
            menu = customerMenu;
        }
        while (this.currentUser != null && menu != null)
            menu.Show();
    }

    public static void main(String args[]) {
        (new ATM()).Run();
    }

    private ATM Loader(Scanner in) {
        try {
            GregorianCalendar now = new GregorianCalendar();
            now.setTime(new Date(in.nextLong()));
            now.add(Calendar.DATE, 1);
            this.initClock = new GregorianCalendar();
            this.initTime = now;
        } catch (Exception e) {
        }
        return this;
    }

    @Override
    public void Write(PrintWriter out) throws IOException {
        out.write(String.valueOf(this.getCurrentTime().getTime().getTime()));
    }

}
