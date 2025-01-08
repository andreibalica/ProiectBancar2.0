package org.poo.packagePOO.Bank.Account.ServicePlan;

import org.poo.packagePOO.CurrencyConverter;

public class StandardPlan implements ServicePlan {
    @Override
    public double calculateCommission(double amount, String currency) {
        double commissionAmount = amount * 0.002; // 0.2%
        return Math.round(commissionAmount * 100.0) / 100.0;
    }

    @Override
    public String getPlanType() {
        return "standard";
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
