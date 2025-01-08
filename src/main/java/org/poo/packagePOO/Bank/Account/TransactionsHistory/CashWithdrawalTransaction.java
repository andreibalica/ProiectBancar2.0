package org.poo.packagePOO.Bank.Account.TransactionsHistory;

public final class CashWithdrawalTransaction extends TransactionHistory {
    private final double amount;

    public CashWithdrawalTransaction(final int timestamp,
                                     final double amount) {
        super(timestamp, String.format("Cash withdrawal of %.1f", amount));
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public void accept(final TransactionVisitor visitor) {
        visitor.visit(this);
    }
}