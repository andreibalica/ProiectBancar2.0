package org.poo.packagePOO.Bank.Account.TransactionsHistory;

public final class InterestRateTransaction extends TransactionHistory {
    private final double newRate;

    /**
     *
     * @param timestamp
     * @param description
     * @param newRate
     */
    public InterestRateTransaction(final int timestamp,
                                   final String description,
                                   final double newRate) {
        super(timestamp, description);
        this.newRate = newRate;
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
    public double getNewRate() {
        return newRate;
    }
}
