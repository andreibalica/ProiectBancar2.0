package org.poo.packagePOO.Bank.Account.ServicePlan;

public class StudentPlan implements ServicePlan {
    @Override
    public double calculateCommission(double amount, String currency) {
        return 0.0;
    }

    @Override
    public String getPlanType() {
        return "student";
    }

    @Override
    public double getUpgradeFee(final String targetPlan) {
        switch (targetPlan) {
            case "silver":
                return 100.0;
            case "gold":
                return 350.0;
            default:
                return 0.0;
        }
    }
}

