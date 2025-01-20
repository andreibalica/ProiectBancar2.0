package org.poo.packagePOO.Bank.Account.TransactionsHistory;

public final class WithdrawSavingsTransaction extends TransactionHistory {
    private final String account;
    private final double amount;
    private final String currency;
    private final String error;

    /**
     * @param timestamp
     * @param account
     * @param amount
     * @param currency
     * @param error
     */
    public WithdrawSavingsTransaction(final int timestamp,
                                      final String account,
                                      final double amount,
                                      final String currency,
                                      final String error) {
        super(timestamp, String.format("Withdraw %.2f %s from savings account", amount, currency));
        this.account = account;
        this.amount = amount;
        this.currency = currency;
        this.error = error;
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
    public double getAmount() {
        return amount;
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
    public String getError() {
        return error;
    }

    /**
     * @param visitor
     */
    @Override
    public void accept(final TransactionVisitor visitor) {
        visitor.visit(this);
    }
}
