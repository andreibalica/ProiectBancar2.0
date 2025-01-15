package org.poo.packagePOO.Transaction;

import org.poo.packagePOO.Bank.Account.BankAccount;
import org.poo.packagePOO.Bank.Account.TransactionsHistory.TransactionFactory;
import org.poo.packagePOO.Bank.User;
import org.poo.packagePOO.CurrencyConverter;
import org.poo.packagePOO.GlobalManager;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class WithdrawSavingsStrategy implements TransactionStrategy {
    private final String account;
    private final double amount;
    private final String currency;
    private final int timestamp;
    private String error;
    private BankAccount savingsAccount;
    private BankAccount classicAccount;

    public WithdrawSavingsStrategy(String account, double amount,
                                   String currency, int timestamp) {
        this.account = account;
        this.amount = amount;
        this.currency = currency;
        this.timestamp = timestamp;
    }

    @Override
    public boolean validate() {
        savingsAccount = GlobalManager.getGlobal().getBank().getAccountIBAN(account);
        if (savingsAccount == null) {
            error = "Account not found";
            return false;
        }

        if (!savingsAccount.getAccountType().equals("savings")) {
            error = "Account is not of type savings";
            return false;
        }

        User user = GlobalManager.getGlobal().getBank().getUserEmail(savingsAccount.getEmail());
        if (!validateAge(user)) {
            savingsAccount.addTransactionHistory(
                    TransactionFactory.createWithdrawSavingsTransaction(
                            timestamp,
                            savingsAccount.getIBAN(),
                            amount,
                            currency,
                            "You don't have the minimum age required."
                    )
            );
            return false;
        }

        classicAccount = GlobalManager.getGlobal().getBank().getClassicAccountEmailCurrency(savingsAccount.getEmail(), currency);
        if (classicAccount == null) {
            savingsAccount.addTransactionHistory(
                    TransactionFactory.createErrorTransaction(
                            timestamp,
                            "You do not have a classic account."
                    )
            );
            return false;
        }

        return true;
    }

    @Override
    public boolean process() {
        double convertedAmount;
        try {
            convertedAmount = CurrencyConverter.getConverter()
                    .convert(savingsAccount.getCurrency(), currency, amount);
        } catch (IllegalArgumentException e) {
            return false;
        }

        User user = GlobalManager.getGlobal().getBank().getUserEmail(savingsAccount.getEmail());
        double commission = user.getServicePlan().calculateCommission(amount, currency);

        if (savingsAccount.getBalance() < convertedAmount + commission) {
            error = "Insufficient funds";
            return false;
        }

        savingsAccount.payAmount(convertedAmount + commission);
        classicAccount.addAmount(amount);

        savingsAccount.addTransactionHistory(
                TransactionFactory.createTransferTransaction(
                        timestamp,
                        "Savings withdrawal",
                        savingsAccount.getIBAN(),
                        classicAccount.getIBAN(),
                        amount,
                        currency,
                        true
                )
        );
        return true;
    }

    @Override
    public String getError() {
        return error;
    }

    private boolean validateAge(User user) {
        if (user == null) return false;
        try {
            LocalDate birthDate = LocalDate.parse(user.getBirthDate());
            return Period.between(birthDate, LocalDate.now()).getYears() >= 21;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}