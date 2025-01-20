package org.poo.packagePOO.Transaction;

import org.poo.packagePOO.Bank.Account.BankAccount;
import org.poo.packagePOO.Bank.Account.ServicePlan.GoldPlan;
import org.poo.packagePOO.Bank.Account.ServicePlan.ServicePlan;
import org.poo.packagePOO.Bank.Account.ServicePlan.SilverPlan;
import org.poo.packagePOO.Bank.Account.ServicePlan.StandardPlan;
import org.poo.packagePOO.Bank.Account.ServicePlan.StudentPlan;
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

    /**
     * @param accountIBAN
     * @param newPlanType
     * @param timestamp
     */
    public UpgradePlanStrategy(final String accountIBAN,
                               final String newPlanType,
                               final int timestamp) {
        this.accountIBAN = accountIBAN;
        this.newPlanType = newPlanType;
        this.timestamp = timestamp;
    }

    /**
     * @return
     */
    @Override
    public boolean validate() {
        Bank bank = GlobalManager.getGlobal().getBank();
        BankAccount account = bank.getAccountIBAN(accountIBAN);

        if (account == null) {
            error = "Account not found";
            return false;
        }

        User user = bank.getUserEmail(account.getEmail());
        if (user == null) {
            error = "User not found";
            return false;
        }

        return true;
    }

    /**
     * @return
     */
    @Override
    public boolean process() {
        Bank bank = GlobalManager.getGlobal().getBank();
        BankAccount account = bank.getAccountIBAN(accountIBAN);

        for (User user : bank.getUsers()) {
            if (user.getEmail().equals(account.getEmail())) {
                ServicePlan currentPlan = user.getServicePlan();
                String currentPlanType = currentPlan.getPlanType();

                if (currentPlanType.equals(newPlanType)) {
                    account.addTransactionHistory(
                            TransactionFactory.createErrorTransaction(
                                    timestamp,
                                    "The user already has the " + newPlanType + " plan."
                            )
                    );
                    return false;
                }

                if (currentPlanType.equals("gold")
                        || (currentPlanType.equals("silver") && !newPlanType.equals("gold"))
                        || ((currentPlanType.equals("student")
                        || currentPlanType.equals("standard"))
                        && (newPlanType.equals("student")
                        || newPlanType.equals("standard")))) {
                    account.addTransactionHistory(
                            TransactionFactory.createErrorTransaction(
                                    timestamp,
                                    "You cannot downgrade your plan."
                            )
                    );
                    return false;
                }

                double upgradeFee = currentPlan.getUpgradeFee(newPlanType);

                if (!account.getCurrency().equals("RON")) {
                    upgradeFee = CurrencyConverter.getConverter()
                            .convert("RON", account.getCurrency(), upgradeFee);
                }

                if (account.getBalance() < upgradeFee) {
                    account.addTransactionHistory(
                            TransactionFactory.createErrorTransaction(
                                    timestamp,
                                    "Insufficient funds"
                            )
                    );
                    return false;
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
                account.payAmount(upgradeFee);

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

    /**
     * @return
     */
    @Override
    public String getError() {
        return error;
    }
}
