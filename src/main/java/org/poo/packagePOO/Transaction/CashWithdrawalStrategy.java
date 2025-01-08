package org.poo.packagePOO.Transaction;

import org.poo.packagePOO.Bank.Account.BankAccount;
import org.poo.packagePOO.Bank.Account.TransactionsHistory.TransactionFactory;
import org.poo.packagePOO.Bank.Card;
import org.poo.packagePOO.Bank.User;
import org.poo.packagePOO.CurrencyConverter;
import org.poo.packagePOO.GlobalManager;

public class CashWithdrawalStrategy implements TransactionStrategy {
    private final String cardNumber;
    private final double amount;
    private final String email;
    private final String location;
    private final int timestamp;
    private String error;
    private BankAccount account;

    public CashWithdrawalStrategy(final String cardNumber,
                                  final double amount,
                                  final String email,
                                  final String location,
                                  final int timestamp) {
        this.cardNumber = cardNumber;
        this.amount = amount;
        this.email = email;
        this.location = location;
        this.timestamp = timestamp;
    }

    @Override
    public boolean validate() {
        account = GlobalManager.getGlobal().getBank().getAccountCard(cardNumber);
        if (account == null) {
            error = "Card not found";
            return false;
        }

        Card card = account.searchCard(cardNumber);
        if (!card.getEmail().equals(email)) {
            error = "Card not owned by user";
            return false;
        }

        if (card.getStatus().equals("frozen")) {
            account.addTransactionHistory(TransactionFactory
                    .createErrorTransaction(timestamp, "The card is frozen"));
            return false;
        }

        return true;
    }

    @Override
    public boolean process() {
        User user = GlobalManager.getGlobal().getBank().getUserEmail(email);
        if (user == null) {
            error = "User not found";
            return false;
        }

        double withdrawalInAccountCurrency;
        try {
            withdrawalInAccountCurrency = CurrencyConverter.getConverter()
                    .convert("RON", account.getCurrency(), amount);
        } catch (IllegalArgumentException e) {
            return false;
        }

        double commission = user.getServicePlan()
                .calculateCommission(withdrawalInAccountCurrency, account.getCurrency());

        withdrawalInAccountCurrency = Math.round(withdrawalInAccountCurrency * 100.0) / 100.0;
        commission = Math.round(commission * 100.0) / 100.0;

        if (account.getBalance() < withdrawalInAccountCurrency + commission) {
            account.addTransactionHistory(TransactionFactory
                    .createErrorTransaction(timestamp, "Insufficient funds"));
            return false;
        }

        account.payAmount(withdrawalInAccountCurrency + commission);
        account.addTransactionHistory(TransactionFactory
                .createCashWithdrawalTransaction(timestamp, amount));

        return true;
    }

    @Override
    public String getError() {
        return error;
    }
}