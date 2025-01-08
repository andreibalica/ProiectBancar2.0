package org.poo.packagePOO.Bank;

public class Commerciant {
    private final String name;
    private final int id;
    private final String account;
    private final String type;
    private final String cashbackStrategy;

    public Commerciant(String name, int id, String account, String type, String cashbackStrategy) {
        this.name = name;
        this.id = id;
        this.account = account;
        this.type = type;
        this.cashbackStrategy = cashbackStrategy;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getAccount() {
        return account;
    }

    public String getType() {
        return type;
    }

    public String getCashbackStrategy() {
        return cashbackStrategy;
    }

    public String getCommerciant() {
        return name;
    }
}
