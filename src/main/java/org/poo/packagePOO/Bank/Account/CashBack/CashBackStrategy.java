package org.poo.packagePOO.Bank.Account.CashBack;

import org.poo.packagePOO.Bank.Account.BankAccount;

public interface CashBackStrategy {
    double calculateCashback(BankAccount account, String commerciantType,
                             double amount, String userPlanType);
    void trackTransaction(BankAccount account, String commerciantName, double amount);
}