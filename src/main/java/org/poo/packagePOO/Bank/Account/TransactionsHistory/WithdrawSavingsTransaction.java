package org.poo.packagePOO.Bank.Account.TransactionsHistory;

public class WithdrawSavingsTransaction extends TransactionHistory{
    private final String account;
    private final double amount;
    private final String currency;
    private final String error;

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

    public String getAccount() {
        return account;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getError() {
        return error;
    }

    @Override
    public void accept(final TransactionVisitor visitor) {
        visitor.visit(this);
    }
}
