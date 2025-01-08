package org.poo.packagePOO.Bank.Account.TransactionsHistory;

public interface Visitable {
    /**
     *
     * @param visitor
     */
    void accept(TransactionVisitor visitor);
}
