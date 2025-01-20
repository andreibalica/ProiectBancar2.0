package org.poo.packagePOO.Bank.Account.TransactionsHistory;

public final class InterestTransaction extends TransactionHistory {
    private final double amount;
    private final String currency;

    /**
     *
     * @param timestamp
     * @param description
     * @param amount
     */
    public InterestTransaction(final int timestamp,
                               final String currency,
                               final String description,
                               final double amount) {
        super(timestamp, description);
        this.currency = currency;
        this.amount = amount;
    }

    /**
     *
     * @param visitor
     */
    @Override
    public void accept(final TransactionVisitor visitor) {
        visitor.visit(this);
    }

    /**
     *
     * @return
     */
    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }
}
