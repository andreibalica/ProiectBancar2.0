package org.poo.packagePOO.Bank.Account.TransactionsHistory;

public final class UpgradePlanTransaction extends TransactionHistory {
    private final String accountIBAN;
    private final String newPlanType;

    public UpgradePlanTransaction(final int timestamp,
                                  final String description,
                                  final String accountIBAN,
                                  final String newPlanType) {
        super(timestamp, description);
        this.accountIBAN = accountIBAN;
        this.newPlanType = newPlanType;
    }

    public String getAccountIBAN() {
        return accountIBAN;
    }

    public String getNewPlanType() {
        return newPlanType;
    }

    @Override
    public void accept(final TransactionVisitor visitor) {
        visitor.visit(this);
    }
}
