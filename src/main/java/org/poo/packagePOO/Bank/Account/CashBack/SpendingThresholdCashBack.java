package org.poo.packagePOO.Bank.Account.CashBack;

import org.poo.packagePOO.Bank.Account.BankAccount;

public final class SpendingThresholdCashBack implements CashBackStrategy {
    private static final double THRESHOLD_500 = 500.0;
    private static final double THRESHOLD_300 = 300.0;
    private static final double THRESHOLD_100 = 100.0;

    private static final double GOLD_500_RATE = 0.007;
    private static final double SILVER_500_RATE = 0.005;
    private static final double STANDARD_500_RATE = 0.0025;

    private static final double GOLD_300_RATE = 0.0055;
    private static final double SILVER_300_RATE = 0.004;
    private static final double STANDARD_300_RATE = 0.002;

    private static final double GOLD_100_RATE = 0.005;
    private static final double SILVER_100_RATE = 0.003;
    private static final double STANDARD_100_RATE = 0.001;

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
        double totalSpent = account.getSpentForCommerciant(commerciantType);

        if (totalSpent >= THRESHOLD_500
                && !account.hasReceivedCashback(commerciantType + "_500RON")) {
            account.markCashbackReceived(commerciantType + "_500RON");
            return amount * (userPlanType.equals("gold") ? GOLD_500_RATE :
                    userPlanType.equals("silver") ? SILVER_500_RATE : STANDARD_500_RATE);
        }

        if (totalSpent >= THRESHOLD_300
                && !account.hasReceivedCashback(commerciantType + "_300RON")) {
            account.markCashbackReceived(commerciantType + "_300RON");
            return amount * (userPlanType.equals("gold") ? GOLD_300_RATE :
                    userPlanType.equals("silver") ? SILVER_300_RATE : STANDARD_300_RATE);
        }

        if (totalSpent >= THRESHOLD_100
                && !account.hasReceivedCashback(commerciantType + "_100RON")) {
            account.markCashbackReceived(commerciantType + "_100RON");
            return amount * (userPlanType.equals("gold") ? GOLD_100_RATE :
                    userPlanType.equals("silver") ? SILVER_100_RATE : STANDARD_100_RATE);
        }

        return 0;
    }

    /**
     *
     * @param account
     * @param commerciantName
     * @param amountInRON
     */
    @Override
    public void trackTransaction(final BankAccount account,
                                 final String commerciantName,
                                 final double amountInRON) {
        account.addSpendingThresholdTotal(commerciantName, amountInRON);
    }
}
