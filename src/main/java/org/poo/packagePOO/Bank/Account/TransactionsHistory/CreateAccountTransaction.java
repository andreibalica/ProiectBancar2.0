package org.poo.packagePOO.Bank.Account.TransactionsHistory;

public final class CreateAccountTransaction extends TransactionHistory {
    /**
     *
     * @param timestamp
     * @param description
     */
    public CreateAccountTransaction(final int timestamp,
                                    final String description) {
        super(timestamp, description);
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
