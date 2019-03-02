import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ATM {
    private Date currentTime;
    private Date systemClock;
    private boolean powerON = true;
    private Console console = new Console();

    private int numOfCash5 = 100;
    private int numOfCash10 = 100;
    private int numOfCash20 = 100;
    private int numOfCash50 = 100;

    private Map<String, Person> users = new HashMap<String, Person>();
    private List<Transaction> transactionList = new ArrayList<Transaction>();

    private Person currentUser = null;

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

    private void Run() {
        console.Println("\n\nATM is starting ...");
        this.Init();
        while (powerON)
            this.Login();
        console.Println("ATM shutdown ...");
    }

    private String now() {
        Date now = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
        return ft.format(now);
    }

    private void Init() {
        console.Println("ATM is initializing ...");
        while (this.users.size() == 0) {
            String username = console.GetInput("\nPlease input manager's username: ");
            if (!Person.IsValidName(username)) {
                console.showError("invalid username");
                continue;
            }
            String password = console.GetPassword("Please input password: ");
            if (!Person.IsValidPassword(password)) {
                console.showError("invalid password");
                continue;
            }
            String password2 = console.GetPassword("Please input password again: ");
            if (!password.equals(password2)) {
                console.showError("not the same password");
                continue;
            }
            users.put(username, new Manager(username, password));
            console.ShowInfo("Manager " + username + " added");
        }
    }

    private void Login() {
        console.Println("\n\nWelcome to use ATM!");
        console.Println("" + now() + "\n");
        String userName = console.GetInput("Username : ");
        String password = console.GetPassword("Password : ");
        Person person = this.users.get(userName);
        if (person == null || !person.CheckPassword(password)) {
            console.showError("username or password is incorrect");
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
