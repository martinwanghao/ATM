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
}
