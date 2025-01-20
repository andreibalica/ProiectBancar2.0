package org.poo.packagePOO.Bank.Account.ServicePlan;

public final class StudentPlan implements ServicePlan {
    private static final double SILVER_UPGRADE_FEE = 100.0;
    private static final double GOLD_UPGRADE_FEE = 350.0;
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
        return "student";
    }

    /**
     *
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
