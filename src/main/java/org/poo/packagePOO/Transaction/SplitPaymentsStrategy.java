package org.poo.packagePOO.Transaction;

import org.poo.packagePOO.Bank.Account.BankAccount;
import org.poo.packagePOO.Bank.Account.TransactionsHistory.TransactionFactory;
import org.poo.packagePOO.CurrencyConverter;
import org.poo.packagePOO.GlobalManager;

import java.util.ArrayList;
import java.util.ListIterator;

public final class SplitPaymentsStrategy implements TransactionStrategy {
    private final ArrayList<String> accounts;
    private final double totalAmount;
    private final String currency;
    private final int timestamp;
    private double convertedAmount;
    private String error;

    /**
     *
     * @param accounts
     * @param totalAmount
     * @param currency
     * @param timestamp
     */
    public SplitPaymentsStrategy(final ArrayList<String> accounts,
                                 final double totalAmount,
                                 final String currency,
                                 final int timestamp) {
        this.accounts = accounts;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.timestamp = timestamp;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean validate() {
        double amountPerParticipant = totalAmount / accounts.size();
        String errorAccount = null;

        ListIterator<String> iterator = accounts.listIterator(accounts.size());
        while (iterator.hasPrevious()) {
            String accountIBAN = iterator.previous();
            BankAccount account = GlobalManager.getGlobal()
                    .getBank().getAccountIBAN(accountIBAN);
            if (account == null) {
                error = "Account not found";
                return false;
            }

            convertedAmount = amountPerParticipant;
            try {
                convertedAmount = CurrencyConverter.getConverter()
                        .convert(currency, account.getCurrency(), amountPerParticipant);
            } catch (IllegalArgumentException e) {
                return false;
            }

            if (account.getBalance() < convertedAmount) {
                errorAccount = accountIBAN;
                break;
            }
        }

        if (errorAccount != null) {
            for (String involvedIBAN : accounts) {
                BankAccount involvedAccount = GlobalManager.getGlobal()
                        .getBank().getAccountIBAN(involvedIBAN);
                if(involvedAccount == null) {
                    continue;
                }
                involvedAccount.addTransactionHistory(
                        TransactionFactory.createSplitPaymentTransaction(
                                timestamp,
                                totalAmount,
                                amountPerParticipant,
                                currency,
                                accounts,
                                "Account " + errorAccount
                                        + " has insufficient funds for a split payment."
                        )
                );
            }
            return false;
        }

        return true;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean process() {
        double amountPerParticipant = totalAmount / accounts.size();

        for (String accountIBAN : accounts) {
            BankAccount account = GlobalManager.getGlobal()
                    .getBank().getAccountIBAN(accountIBAN);

            convertedAmount = CurrencyConverter.getConverter()
                    .convert(currency, account.getCurrency(), amountPerParticipant);

            account.payAmount(convertedAmount);
            account.addTransactionHistory(
                    TransactionFactory.createSplitPaymentTransaction(
                            timestamp,
                            totalAmount,
                            amountPerParticipant,
                            currency,
                            accounts,
                            null
                    )
            );
        }
        return true;
    }

    /**
     *
     * @return
     */
    @Override
    public String getError() {
        return error;
    }
}
