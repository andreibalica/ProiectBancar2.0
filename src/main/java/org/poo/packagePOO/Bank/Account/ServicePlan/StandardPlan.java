package org.poo.packagePOO.Bank.Account.ServicePlan;

public final class StandardPlan implements ServicePlan {
    private static final double COMMISSION_RATE = 0.002;
    private static final double SILVER_UPGRADE_FEE = 100.0;
    private static final double GOLD_UPGRADE_FEE = 350.0;

    /**
     * @param amount
     * @param currency
     * @return
     */
    @Override
    public double calculateCommission(final double amount, final String currency) {
        return amount * COMMISSION_RATE;
    }

    /**
     * @return
     */
    @Override
    public String getPlanType() {
        return "standard";
    }

    /**
     * @param targetPlan
     * @return
     */
    @Override
    public double getUpgradeFee(final String targetPlan) {
        switch (targetPlan) {
            case "silver":
                return SILVER_UPGRADE_FEE;
            case "gold":
                return GOLD_UPGRADE_FEE;
            default:
                return 0.0;
        }
    }
}
