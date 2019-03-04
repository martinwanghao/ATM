import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ATM implements Save {
    private GregorianCalendar initTime;
    private GregorianCalendar initClock;
    private boolean powerON = true;
    private Screen screen = new Screen();
    private Disk disk = new Disk();

    private int numOfCash5 = 100;
    private int numOfCash10 = 100;
    private int numOfCash20 = 100;
    private int numOfCash50 = 100;

    private Map<String, User> users = new HashMap<String, User>();
    private List<Transaction> transactionList = new ArrayList<Transaction>();

    private User currentUser = null;
    private static Pattern patternTime = Pattern
            .compile("^(?<year>\\d{4})-(?<month>\\d{1,2})-(?<day>\\d{1,2})\\s+(?<hour>\\d{1,2}):(?<minute>\\d{1,2})$");

    private Menu managerMenu;
    private Menu customerMenu;

    private Menu getManagerMenu() {
        if (managerMenu == null) {
            managerMenu = new Menu("Manager Menu", this.screen);
            managerMenu.AddOption("Create a customer account", () -> {
                String username = screen.GetInput("\nPlease input customer's username: ");
                if (!User.IsValidName(username)) {
                    screen.ShowConfirmMsg("ERROR: invalid username");
                    return true;
                }

                String password = String.valueOf(100000 + (int) (Math.random() * 100000));
                users.put(username, new Customer(username, password));
                screen.ShowConfirmMsg(
                        "SUCCESS: Customer account '" + username + "' added, password is '" + password + "'");
                return true;
            });
            managerMenu.AddOption("Change password", () -> {
                ChangePassword();
                return true;
            });
            managerMenu.AddOption("Shutdown this ATM", () -> {
                screen.ShowMsg("\nATM shutdown ...");
                this.Shutdown();
                this.powerON = false;
                return false;
            });
            managerMenu.AddOption("Logout", null);
        }
        return managerMenu;
    }

    private Menu getCustomerMenu() {
        if (customerMenu == null) {
            customerMenu = new Menu("Customer Menu", this.screen);
            customerMenu.AddOption("Change password", () -> {
                ChangePassword();
                return true;
            });
            customerMenu.AddOption("Logout", null);
        }
        return customerMenu;
    }

    private void ChangePassword() {
        String passwordOld = screen.GetPassword("Old password: ", "");
        if (!this.currentUser.CheckPassword(passwordOld)) {
            screen.ShowConfirmMsg("ERROR: password is wrong");
            return;
        }
        String password = screen.GetPassword("New password: ");
        if (!User.IsValidPassword(password)) {
            screen.ShowConfirmMsg("ERROR: invalid password");
            return;
        }
        String password2 = screen.GetPassword("New password again: ", "");
        if (!password.equals(password2)) {
            screen.ShowConfirmMsg("ERROR: The two passwords you typed do not match");
            return;
        }
        this.currentUser.setPassword(password);
        screen.ShowConfirmMsg("SUCCESS: Password changed");
    }

    private GregorianCalendar getCurrentTime() {
        GregorianCalendar now = new GregorianCalendar();
        now.setTime(new Date((new Date()).getTime() - initClock.getTime().getTime() + initTime.getTime().getTime()));
        return now;
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

    private void Run() {
        screen.ShowMsg("\n\n");
        screen.ShowMsg("╔═════════════════════════════════╗");
        screen.ShowMsg("║                                 ║");
        screen.ShowMsg("║        Flora's ATM v2.1         ║");
        screen.ShowMsg("║                                 ║");
        screen.ShowMsg("╚═════════════════════════════════╝");
        screen.ShowMsg("\nATM powered ON");

        this.Load();

        while (powerON)
            this.Login();

        screen.ShowMsg("\n\nATM powered OFF\n");
    }

    private String now() {
        SimpleDateFormat ft = new SimpleDateFormat("E yyyy-MM-dd 'at' hh:mm:ss a zzz");
        return ft.format(getCurrentTime().getTime());
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

        screen.ShowMsg("" + now());
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

    private void Shutdown() {
        File f = new File("./DISK");
        f.mkdir();
        try {
            disk.Save("./DISK/ATM.txt", this);
            screen.ShowMsg("Saving ATM's time ... ok");
        } catch (IOException e) {
            screen.ShowMsg("Saving ATM's time ... failed(" + e.getMessage() + ")");
        }

        try {
            disk.Save("./DISK/Users.txt", new ArrayList<Save>(this.users.values()));
            screen.ShowMsg("Saving user acounts ... ok, " + this.users.size() + " account(s) saved");
        } catch (IOException e) {
            screen.ShowMsg("Saving user acounts ... failed(" + e.getMessage() + ")");
        }
    }

    private void Login() {
        screen.ShowMsg("\n\nWelcome to Flora's ATM");
        screen.ShowMsg("" + now() + "\n");
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
        if (user instanceof Manager) {
            this.getManagerMenu().Show();
        } else if (user instanceof Customer) {
            this.getCustomerMenu().Show();
        }
        this.currentUser = null;
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
