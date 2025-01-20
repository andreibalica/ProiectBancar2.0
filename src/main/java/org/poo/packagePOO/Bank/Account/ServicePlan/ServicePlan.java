package org.poo.packagePOO.Bank.Account.ServicePlan;

public interface ServicePlan {
    /**
     * @param amount
     * @param currency
     * @return
     */
    double calculateCommission(double amount, String currency);
    /**
     * @return
     */
    String getPlanType();
    /**
     * @param targetPlan
     * @return
     */
    double getUpgradeFee(String targetPlan);
}
