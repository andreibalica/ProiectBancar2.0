package org.poo.packagePOO.Bank.Account.CashBack;

import org.poo.packagePOO.Bank.Account.BankAccount;

public class SpendingThresholdCashBack implements CashBackStrategy {
    @Override
    public double calculateCashback(BankAccount account, String commerciantType,
                                    double amount, String userPlanType) {
        double totalSpent = account.getSpentForMerchant(commerciantType);

        if (totalSpent >= 500 && !account.hasReceivedCashback(commerciantType + "_500RON")) {
            account.markCashbackReceived(commerciantType + "_500RON");
            return amount * (userPlanType.equals("gold") ? 0.007 :
                    userPlanType.equals("silver") ? 0.005 : 0.0025);
        }

        if (totalSpent >= 300 && !account.hasReceivedCashback(commerciantType + "_300RON")) {
            account.markCashbackReceived(commerciantType + "_300RON");
            return amount * (userPlanType.equals("gold") ? 0.0055 :
                    userPlanType.equals("silver") ? 0.004 : 0.002);
        }

        if (totalSpent >= 100 && !account.hasReceivedCashback(commerciantType + "_100RON")) {
            account.markCashbackReceived(commerciantType + "_100RON");
            return amount * (userPlanType.equals("gold") ? 0.005 :
                    userPlanType.equals("silver") ? 0.003 : 0.001);
        }

        return 0;
    }

    @Override
    public void trackTransaction(BankAccount account, String commerciantName, double amountInRON) {
        account.addSpendingThresholdTotal(commerciantName, amountInRON);
    }
}