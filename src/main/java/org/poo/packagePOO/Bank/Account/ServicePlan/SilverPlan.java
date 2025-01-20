package org.poo.packagePOO.Bank.Account.ServicePlan;

import org.poo.packagePOO.CurrencyConverter;

public final class SilverPlan implements ServicePlan {
    private static final double COMMISSION_THRESHOLD = 500.0;
    private static final double COMMISSION_RATE = 0.001;
    private static final double GOLD_UPGRADE_FEE = 250.0;

    /**
     *
     * @param amount
     * @param currency
     * @return
     */
    @Override
    public double calculateCommission(final double amount, final String currency) {
        double amountInRON = CurrencyConverter.getConverter().convert(currency, "RON", amount);
        if (amountInRON >= COMMISSION_THRESHOLD) {
            return amount * COMMISSION_RATE;
        }
        return 0.0;
    }

    /**
     *
     * @return
     */
    @Override
    public String getPlanType() {
        return "silver";
    }

    /**
     *
     * @param targetPlan
     * @return
     */
    @Override
    public double getUpgradeFee(final String targetPlan) {
        if ("gold".equals(targetPlan)) {
            return GOLD_UPGRADE_FEE;
        }
        return 0.0;
    }
}
