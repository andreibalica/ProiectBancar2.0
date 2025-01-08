package org.poo.packagePOO.Bank.Account.ServicePlan;

public interface ServicePlan {
    double calculateCommission(double amount, String currency);
    String getPlanType();
    double getUpgradeFee(String targetPlan);
}
