package org.poo.packagePOO.Bank.Account.TransactionsHistory;

import java.util.ArrayList;

public final class SplitPaymentsTransaction extends TransactionHistory {
    private final double totalAmount;
    private final double splitAmount;
    private final String currency;
    private final ArrayList<String> involvedAccounts;
    private final String error;

    /**
     *
     * @param timestamp
     * @param totalAmount
     * @param splitAmount
     * @param currency
     * @param accounts
     * @param error
     */
    public SplitPaymentsTransaction(final int timestamp,
                                    final double totalAmount,
                                    final double splitAmount,
                                    final String currency,
                                    final ArrayList<String> accounts,
                                    final String error) {
        super(timestamp, String.format("Split payment of %.2f %s", totalAmount, currency));
        this.totalAmount = totalAmount;
        this.splitAmount = splitAmount;
        this.currency = currency;
        this.involvedAccounts = new ArrayList<>(accounts);
        this.error = error;
    }

    /**
     *
     * @return
     */
    public double getSplitAmount() {
        return splitAmount;
    }

    /**
     *
     * @return
     */
    public String getCurrency() {
        return currency;
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getInvolvedAccounts() {
        return involvedAccounts;
    }

    /**
     *
     * @return
     */
    public String getError() {
        return error;
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
