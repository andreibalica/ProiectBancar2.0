package org.poo.packagePOO.Bank.Account.TransactionsHistory;

public final class ErrorTransaction extends TransactionHistory {
    private final String errorMessage;

    /**
     *
     * @param timestamp
     * @param description
     */
    public ErrorTransaction(final int timestamp,
                            final String description) {
        super(timestamp, description);
        this.errorMessage = description;
    }

    /**
     *
     * @param visitor
     */
    @Override
    public void accept(final TransactionVisitor visitor) {
        visitor.visit(this);
    }
}
