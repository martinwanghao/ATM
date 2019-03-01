import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ATM {
    private Date currentTime;
    private Date systemClock;
    private Menu menu;

    private int numOfCash5 = 100;
    private int numOfCash10 = 100;
    private int numOfCash20 = 100;
    private int numOfCash50 = 100;

    private Map<String, Customer> customerMap = new HashMap<String, Customer>();
    private Map<String, Manager> managerMap = new HashMap<String, Manager>();
    private List<Transaction> transactionList = new ArrayList<Transaction>();

    private Person currentUser = null;

    public ATM() {
        menu = new Menu("ATM Main Menu", new Option("Customer", () -> {

        }), new Option("Manager", () -> {
        }));
    }

    private void Run() {
        System.out.println("ATM starting ...");
        this.ShowMenu();
    }

    private void ShowMenu() {
        (new Menu("ATM Main", new Option("Customer", () -> {
            this.CustomerLogin();
        }), new Menu("Manager"))).Run();
    }

    private void CustomerLogin() {
        String name = GetInput("Person name");
        if (CheckPassword(name, GetInput("Password"))) {
            (new Menu("Hello " + name, new Option("Show balances", () -> {
            }))).Run();
        }
    }

    private boolean CheckPassword(String name, String password) {
        return false;
    }

    private String GetInput(String message) {
        return null;
    }

    public static void main(String args[]) {
        (new ATM()).Run();
    }
}
