package org.poo.packagePOO.Bank.Account.ServicePlan;

public final class GoldPlan implements ServicePlan {
    /**
     *
     * @param amount
     * @param currency
     * @return
     */
    @Override
    public double calculateCommission(final double amount, final String currency) {
        return 0.0;
    }

    /**
     *
     * @return
     */
    @Override
    public String getPlanType() {
        return "gold";
    }

    /**
     *
     * @param targetPlan
     * @return
     */
    @Override
    public double getUpgradeFee(final String targetPlan) {
        return 0.0;
    }
}
