import java.util.Date;

public enum AccountType {
    CreditCards("Credit Cards Accounts", 1), LineOfCredit("Line of Credit", 2), Chequing("Chequing", 3),
    Savings("Savings", 4);

    private String name;
    private int value;

    private AccountType(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public int getIndex() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static AccountType valueOf(int value) {
        switch (value) {
        case 1:
            return CreditCards;
        case 2:
            return LineOfCredit;
        case 3:
            return Chequing;
        case 4:
            return Savings;
        }
        return null;
    }

    public static AccountType valueOf(Account account) {
        return valueOf(account.getClass().getName());
    }

    public Account CreateAccount(String username, String num, Date time) {
        switch (value) {
        case 1:
            return new CreditCards(username, num, time);
        case 2:
            return new LineOfCredit(username, num, time);
        case 3:
            return new Chequing(username, num, time);
        case 4:
            return new Savings(username, num, time);
        }
        return null;
    }
}
