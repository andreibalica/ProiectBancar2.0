package org.poo.packagePOO.Bank.Account.ServicePlan;

public class GoldPlan implements ServicePlan {
    @Override
    public double calculateCommission(double amount, String currency) {
        return 0.0;
    }

    @Override
    public String getPlanType() {
        return "gold";
    }

    @Override
    public double getUpgradeFee(String targetPlan) {
        return 0.0;
    }
}
