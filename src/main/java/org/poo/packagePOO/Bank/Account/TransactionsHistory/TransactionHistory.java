package org.poo.packagePOO.Bank.Account.TransactionsHistory;

public abstract class TransactionHistory implements Visitable {
    protected final int timestamp;
    protected final String description;

    /**
     *
     * @param timestamp
     * @param description
     */
    public TransactionHistory(final int timestamp, final String description) {
        this.timestamp = timestamp;
        this.description = description;
    }

    /**
     *
     * @return
     */
    public int getTimestamp() {
        return timestamp;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @return
     */
    public boolean isPayment() {
        return false;
    }

    @Override
    public abstract void accept(TransactionVisitor visitor);
}
