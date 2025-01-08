package org.poo.packagePOO.Bank.Account.TransactionsHistory;

public interface TransactionVisitor {
    /**
     *
     * @param transaction
     */
    void visit(CreateAccountTransaction transaction);

    /**
     *
     * @param transaction
     */
    void visit(CardCreateDeleteTransaction transaction);

    /**
     *
     * @param transaction
     */
    void visit(MoneyTransferTransaction transaction);

    /**
     *
     * @param transaction
     */
    void visit(CardPaymentTransaction transaction);

    /**
     *
     * @param transaction
     */
    void visit(ErrorTransaction transaction);

    /**
     *
     * @param transaction
     */
    void visit(SplitPaymentsTransaction transaction);

    /**
     *
     * @param transaction
     */
    void visit(InterestTransaction transaction);

    /**
     *
     * @param transaction
     */
    void visit(InterestRateTransaction transaction);

    void visit(WithdrawSavingsTransaction transaction);

    void visit(UpgradePlanTransaction transaction);

    void visit(CashWithdrawalTransaction transaction);
}
