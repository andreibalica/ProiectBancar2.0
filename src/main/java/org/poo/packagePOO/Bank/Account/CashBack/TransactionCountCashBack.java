package org.poo.packagePOO.Bank.Account.CashBack;

import org.poo.packagePOO.Bank.Account.BankAccount;

public final class TransactionCountCashBack implements CashBackStrategy {
    private static final int FOOD_THRESHOLD = 2;
    private static final int CLOTHES_THRESHOLD = 5;
    private static final int TECH_THRESHOLD = 10;
    private static final double FOOD_CASHBACK = 0.02;
    private static final double CLOTHES_CASHBACK = 0.05;
    private static final double TECH_CASHBACK = 0.10;

    /**
     *
     * @param account
     * @param commerciantType
     * @param amount
     * @param userPlanType
     * @return
     */
    @Override
    public double calculateCashback(final BankAccount account,
                                    final String commerciantType,
                                    final double amount,
                                    final String userPlanType) {
        if (account.hasReceivedCashback(commerciantType)) {
            return 0;
        }

        int totalTransactions = account.getTransactionCount(commerciantType);

        if ((commerciantType.equals("Food") && totalTransactions >= FOOD_THRESHOLD)
                || (commerciantType.equals("Clothes")
                && totalTransactions >= CLOTHES_THRESHOLD)
                || (commerciantType.equals("Tech")
                && totalTransactions >= TECH_THRESHOLD)) {
            account.markCashbackReceived(commerciantType);
            return amount * getCashbackPercentage(commerciantType);
        }
        return 0;
    }

    /**
     *
     * @param account
     * @param commerciantName
     * @param amount
     */
    @Override
    public void trackTransaction(final BankAccount account,
                                 final String commerciantName,
                                 final double amount) {
        account.incrementTransactionCount(commerciantName);
    }

    /**
     *
     * @param commerciantType
     * @return
     */
    private double getCashbackPercentage(final String commerciantType) {
        switch (commerciantType) {
            case "Food":
                return FOOD_CASHBACK;
            case "Clothes":
                return CLOTHES_CASHBACK;
            case "Tech":
                return TECH_CASHBACK;
            default:
                return 0;
        }
    }
}
