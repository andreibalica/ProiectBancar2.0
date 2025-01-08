package org.poo.packagePOO.Bank.Account.CashBack;

import org.poo.packagePOO.Bank.Account.BankAccount;

public class TransactionCountCashBack implements CashBackStrategy {
    @Override
    public double calculateCashback(BankAccount account, String commerciantType,
                                    double amount, String userPlanType) {
        if (account.hasReceivedCashback(commerciantType)) {
            return 0;
        }

        int totalTransactions = account.getTransactionCount(commerciantType);

        if ((commerciantType.equals("Food") && totalTransactions >= 2) ||
                (commerciantType.equals("Clothes") && totalTransactions >= 5) ||
                (commerciantType.equals("Tech") && totalTransactions >= 10)) {

            account.markCashbackReceived(commerciantType);
            return amount * getCashbackPercentage(commerciantType);
        }
        return 0;
    }

    @Override
    public void trackTransaction(BankAccount account, String commerciantName, double amount) {
        account.incrementTransactionCount(commerciantName);
    }

    private double getCashbackPercentage(String commerciantType) {
        switch(commerciantType) {
            case "Food": return 0.02;
            case "Clothes": return 0.05;
            case "Tech": return 0.10;
            default: return 0;
        }
    }
}