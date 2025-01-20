package org.poo.packagePOO.Bank;

public final class Commerciant {
    private final String name;
    private final int id;
    private final String account;
    private final String type;
    private final String cashbackStrategy;

    /**
     * @param name
     * @param id
     * @param account
     * @param type
     * @param cashbackStrategy
     */
    public Commerciant(final String name,
                       final int id,
                       final String account,
                       final String type,
                       final String cashbackStrategy) {
        this.name = name;
        this.id = id;
        this.account = account;
        this.type = type;
        this.cashbackStrategy = cashbackStrategy;
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * @return
     */
    public String getAccount() {
        return account;
    }

    /**
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * @return
     */
    public String getCashbackStrategy() {
        return cashbackStrategy;
    }

    /**
     * @return
     */
    public String getCommerciant() {
        return name;
    }
}
