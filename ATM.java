import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
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
    private Map<String, Application> applications = new HashMap<String, Application>();
    private Map<String, Account> accounts = new HashMap<String, Account>();

    // private int numOfCash5 = 100;
    // private int numOfCash10 = 100;
    // private int numOfCash20 = 100;
    // private int numOfCash50 = 100;
    // private List<Transaction> transactionList = new ArrayList<Transaction>();

    private static final Pattern patternTime = Pattern
            .compile("^(?<year>\\d{4})-(?<month>\\d{1,2})-(?<day>\\d{1,2})\\s+(?<hour>\\d{1,2}):(?<minute>\\d{1,2})$");

    public Screen getScreen() {
        return this.screen;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public List<Application> getApplications() {
        return new ArrayList<Application>(this.applications.values());
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

    public static String toShortDateString(Date d) {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return ft.format(d);
    }

    public static String toDateString(Date d) {
        SimpleDateFormat ft = new SimpleDateFormat("E yyyy-MM-dd 'at' hh:mm:ss a zzz");
        return ft.format(d);
    }

    private String getCurrentTimeString() {
        return toDateString(getCurrentTime().getTime());
    }

    private void Run() {
        screen.ShowMsg("\n\n");
        screen.ShowMsg("╔═════════════════════════════════╗");
        screen.ShowMsg("║ :                             : ║");
        screen.ShowMsg("║                                 ║");
        screen.ShowMsg("║           Welcome to            ║");
        screen.ShowMsg("║         FLORA ATM v2.22         ║");
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

    private void ShowDot() {
        this.screen.ShowPartMsg(".");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
    }

    private void LoadObject(Loader<?> loader, String name) {
        screen.ShowPartMsg("Loading " + name + ".txt ");
        try {
            this.disk.LoadObject("./DISK/" + name + ".txt", loader, this::ShowDot);
            screen.ShowMsg(" OK");
        } catch (Exception e) {
            screen.ShowMsg("FAILED, " + e.getMessage());
        }
    }

    private <T> ArrayList<T> Load(Loader<T> loader, String name) {
        screen.ShowPartMsg("Loading " + name + ".txt ");
        try {
            ArrayList<T> list = this.disk.LoadList("./DISK/" + name + ".txt", loader, this::ShowDot);
            screen.ShowMsg(" OK, " + list.size() + " item(s) loaded");
            return list;
        } catch (Exception e) {
            screen.ShowMsg(" FAILED, " + e.getMessage());
        }
        return new ArrayList<T>();
    }

    private void Load() {
        LoadObject(this::Loader, "ATM");
        if (this.initTime == null) {
            while (!InitSetClock())
                ;
        }

        Load(User::Loader, User.class.getName()).forEach(o -> this.users.put(o.getUsername(), o));
        if (this.users.size() == 0) {
            screen.ShowMsg("\nNOTICE: You should set at least one manager account");
            while (!InitSetManagerAccount())
                ;
        }

        Load(Application::Loader, Application.class.getName()).forEach(o -> this.applications.put(o.getKey(), o));
        Load(Account::Loader, Account.class.getName()).forEach(o -> {
            this.accounts.put(o.getNum(), o);
            ((Customer) this.users.get(o.getUsername())).AddAccount(o);
            ;
        });
    }

    private void Save() {
        File f = new File("./DISK");
        f.mkdir();

        Save(this);
        Save(this.users.values(), User.class.getName());
        Save(this.applications.values(), Application.class.getName());
        Save(this.accounts.values(), Account.class.getName());
    }

    private boolean InitSetClock() {
        screen.ShowMsg("NOTICE: You should set ATM's time first");
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

    private void Save(Saver saver) {
        String name = saver.getClass().getName();
        screen.ShowPartMsg("Saving " + name + ".txt ");
        try {
            disk.Save("./DISK/" + name + ".txt", this, this::ShowDot);
            screen.ShowMsg(" OK");
        } catch (IOException e) {
            screen.ShowMsg(" FAILED, " + e.getMessage());
        }
    }

    private <T extends Saver> void Save(Collection<T> list, String name) {
        screen.ShowPartMsg("Saving " + name + ".txt ");
        try {
            disk.Save("./DISK/" + name + ".txt", new ArrayList<Saver>(list), this::ShowDot);
            screen.ShowMsg(" OK, " + list.size() + " item(s) saved");
        } catch (IOException e) {
            screen.ShowMsg(" FAILED, " + e.getMessage());
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
            menu = new ManagerMenu(this);
        } else if (user instanceof Customer) {
            menu = new CustomerMenu(this);
        }
        while (menu != null && this.currentUser != null)
            menu.Show();
    }

    public static void main(String args[]) {
        (new ATM()).Run();
    }

    private ATM Loader(Scanner in) {
        GregorianCalendar now = new GregorianCalendar();
        now.setTime(new Date(in.nextLong()));
        now.add(Calendar.DATE, 1);
        this.initClock = new GregorianCalendar();
        this.initTime = now;
        return this;
    }

    @Override
    public void Write(PrintWriter out) throws IOException {
        out.write(String.valueOf(this.getCurrentTime().getTime().getTime()) + "\n");
    }

    public void ApplyForAccount(AccountType type) throws Exception {
        Application a = new Application(this.currentUser.getUsername(), type);
        if (applications.containsKey(a.getKey()))
            throw new Exception("You had already send the same application");
        applications.put(a.getKey(), a);
    }

    public void AddAccount(Application application) {
        Account a = application.getAccountType().CreateAccount(application.getUsername(), getNewAccountNum(),
                this.getCurrentTime().getTime());
        this.accounts.put(a.getNum(), a);
        this.applications.remove(application.getKey());
        Customer customer = (Customer) this.users.get(application.getUsername());
        customer.AddAccount(a);
    }

    private String getNewAccountNum() {
        while (true) {
            String newnum = String.valueOf(100000 + (int) (Math.random() * 100000));
            if (this.accounts.containsKey(newnum))
                continue;
            return newnum;
        }
    }

    public void Deposit(String accountnum, float amount) {
        Account a = this.accounts.get(accountnum);
        a.addBalance(amount);
    }
}
