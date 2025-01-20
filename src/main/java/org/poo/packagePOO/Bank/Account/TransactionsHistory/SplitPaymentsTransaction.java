package org.poo.packagePOO.Bank.Account.TransactionsHistory;

import java.util.ArrayList;

public final class SplitPaymentsTransaction extends TransactionHistory {
    private final double totalAmount;
    private final String currency;
    private final ArrayList<String> involvedAccounts;
    private final String error;
    private final String splitPaymentType;
    private final ArrayList<Double> amountForUsers;

    /**
     * @param timestamp
     * @param totalAmount
     * @param currency
     * @param accounts
     * @param error
     * @param splitPaymentType
     * @param amountForUsers
     */
    public SplitPaymentsTransaction(final int timestamp,
                                    final double totalAmount,
                                    final String currency,
                                    final ArrayList<String> accounts,
                                    final String error,
                                    final String splitPaymentType,
                                    final ArrayList<Double> amountForUsers) {
        super(timestamp, String.format("Split payment of %.2f %s", totalAmount, currency));
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.involvedAccounts = accounts;
        this.error = error;
        this.splitPaymentType = splitPaymentType;

        if ("equal".equals(splitPaymentType)) {
            this.amountForUsers = new ArrayList<>();
            double amountPerPerson = totalAmount / accounts.size();
            for (int i = 0; i < accounts.size(); i++) {
                this.amountForUsers.add(amountPerPerson);
            }
        } else {
            this.amountForUsers = amountForUsers;
        }
    }

    /**
     * @return
     */
    public double getTotalAmount() {
        return totalAmount;
    }

    /**
     * @return
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * @return
     */
    public ArrayList<String> getInvolvedAccounts() {
        return involvedAccounts;
    }

    /**
     * @return
     */
    public String getError() {
        return error;
    }

    /**
     * @return
     */
    public String getSplitPaymentType() {
        return splitPaymentType;
    }

    /**
     * @return
     */
    public ArrayList<Double> getAmountForUsers() {
        return amountForUsers;
    }

    /**
     * @return
     */
    public double getSplitAmount() {
        if ("equal".equals(splitPaymentType)) {
            return totalAmount / involvedAccounts.size();
        }
        return amountForUsers != null ? amountForUsers.get(0) : 0.0;
    }

    /**
     * @param visitor
     */
    @Override
    public void accept(final TransactionVisitor visitor) {
        visitor.visit(this);
    }
}
