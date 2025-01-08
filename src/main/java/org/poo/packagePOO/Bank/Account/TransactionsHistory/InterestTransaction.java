package org.poo.packagePOO.Bank.Account.TransactionsHistory;

public final class InterestTransaction extends TransactionHistory {
    private final double amount;

    /**
     *
     * @param timestamp
     * @param description
     * @param amount
     */
    public InterestTransaction(final int timestamp,
                               final String description,
                               final double amount) {
        super(timestamp, description);
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
}
