package org.poo.packagePOO.Transaction;

import org.poo.packagePOO.Bank.Account.BankAccount;
import org.poo.packagePOO.Bank.Account.ServicePlan.*;
import org.poo.packagePOO.Bank.Account.TransactionsHistory.TransactionFactory;
import org.poo.packagePOO.Bank.Bank;
import org.poo.packagePOO.Bank.User;
import org.poo.packagePOO.CurrencyConverter;
import org.poo.packagePOO.GlobalManager;

public final class UpgradePlanStrategy implements TransactionStrategy {
    private final String accountIBAN;
    private final String newPlanType;
    private final int timestamp;
    private String error;

    public UpgradePlanStrategy(String accountIBAN, String newPlanType, int timestamp) {
        this.accountIBAN = accountIBAN;
        this.newPlanType = newPlanType;
        this.timestamp = timestamp;
    }

    @Override
    public boolean validate() {
        Bank bank = GlobalManager.getGlobal().getBank();
        BankAccount account = bank.getAccountIBAN(accountIBAN);

        if (account == null) {
            error = "Account not found";
            return false;
        }

        for (User user : bank.getUsers()) {
            if (user.getEmail().equals(account.getEmail())) {
                ServicePlan currentPlan = user.getServicePlan();

                if (currentPlan.getPlanType().equals(newPlanType)) {
                    error = "The user already has the " + newPlanType + " plan.";
                    return false;
                }

                double upgradeFee = currentPlan.getUpgradeFee(newPlanType);
                if (upgradeFee == 0) {
                    error = "You cannot downgrade your plan.";
                    return false;
                }

                double convertedFee = upgradeFee;
                if (!account.getCurrency().equals("RON")) {
                    try {
                        convertedFee = CurrencyConverter.getConverter()
                                .convert("RON", account.getCurrency(), upgradeFee);
                    } catch (IllegalArgumentException e) {
                        error = "Currency conversion error";
                        return false;
                    }
                }

                if (account.getBalance() < convertedFee) {
                    error = "Insufficient funds";
                    return false;
                }

                return true;
            }
        }

        error = "User not found";
        return false;
    }

    @Override
    public boolean process() {
        Bank bank = GlobalManager.getGlobal().getBank();
        BankAccount account = bank.getAccountIBAN(accountIBAN);

        for (User user : bank.getUsers()) {
            if (user.getEmail().equals(account.getEmail())) {
                ServicePlan currentPlan = user.getServicePlan();
                double upgradeFee = currentPlan.getUpgradeFee(newPlanType);

                double convertedFee = upgradeFee;
                if (!account.getCurrency().equals("RON")) {
                    convertedFee = CurrencyConverter.getConverter()
                            .convert("RON", account.getCurrency(), upgradeFee);
                    convertedFee = Math.round(convertedFee * 100.0) / 100.0;
                }

                ServicePlan newPlan;
                switch (newPlanType) {
                    case "silver":
                        newPlan = new SilverPlan();
                        break;
                    case "gold":
                        newPlan = new GoldPlan();
                        break;
                    case "student":
                        newPlan = new StudentPlan();
                        break;
                    default:
                        newPlan = new StandardPlan();
                }
                user.setServicePlan(newPlan);

                account.payAmount(convertedFee);

                account.addTransactionHistory(
                        TransactionFactory.createUpgradePlanTransaction(
                                timestamp,
                                accountIBAN,
                                newPlanType
                        )
                );

                return true;
            }
        }
        return false;
    }

    @Override
    public String getError() {
        return error;
    }
}