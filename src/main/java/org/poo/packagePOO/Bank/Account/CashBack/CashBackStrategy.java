package org.poo.packagePOO.Bank.Account.CashBack;

import org.poo.packagePOO.Bank.Account.BankAccount;

public interface CashBackStrategy {
    /**
     * @param account
     * @param commerciantType
     * @param amount
     * @param userPlanType
     * @return
     */
    double calculateCashback(BankAccount account, String commerciantType,
                             double amount, String userPlanType);

    /**
     * @param account
     * @param commerciantName
     * @param amount
     */
    void trackTransaction(BankAccount account, String commerciantName, double amount);
}
