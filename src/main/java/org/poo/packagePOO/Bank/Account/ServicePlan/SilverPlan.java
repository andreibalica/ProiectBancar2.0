package org.poo.packagePOO.Bank.Account.ServicePlan;

import org.poo.packagePOO.CurrencyConverter;

public class SilverPlan implements ServicePlan {
    @Override
    public double calculateCommission(double amount, String currency) {
        double amountInRON = CurrencyConverter.getConverter().convert(currency, "RON", amount);

        if (amountInRON >= 500) {
            return amount * 0.001;
        }
        return 0.0;
    }

    @Override
    public String getPlanType() {
        return "silver";
    }

    @Override
    public double getUpgradeFee(String targetPlan) {
       switch (targetPlan) {
            case "gold":
                return 250.0;
            default:
                return 0.0;
        }
    }
}
