package org.poo.packagePOO.Transaction;

import org.poo.packagePOO.Bank.Account.BankAccount;
import org.poo.packagePOO.Bank.Account.TransactionsHistory.TransactionFactory;
import org.poo.packagePOO.Bank.Card;
import org.poo.packagePOO.Bank.Commerciant;
import org.poo.packagePOO.Bank.User;
import org.poo.packagePOO.CurrencyConverter;
import org.poo.packagePOO.GlobalManager;
import org.poo.utils.Utils;

public final class PayOnlineStrategy implements TransactionStrategy {
    private final String cardNumber;
    private final double amount;
    private final String currency;
    private final String description;
    private final String commerciant;
    private final String email;
    private final int timestamp;
    private String error;

    /**
     *
     * @param cardNumber
     * @param amount
     * @param currency
     * @param description
     * @param commerciant
     * @param email
     * @param timestamp
     */
    public PayOnlineStrategy(final String cardNumber,
                             final double amount,
                             final String currency,
                             final String description,
                             final String commerciant,
                             final String email,
                             final int timestamp) {
        this.cardNumber = cardNumber;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.commerciant = commerciant;
        this.email = email;
        this.timestamp = timestamp;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean validate() {
        if (amount <= 0) {
            return false;
        }

        for (BankAccount account : GlobalManager.getGlobal().getBank().getAccounts()) {
            for (Card card : account.getCards()) {
                if (card.getCardNumber().equals(cardNumber)) {
                    if (!card.getEmail().equals(email)) {
                        error = "Card not owned by user";
                        return false;
                    }
                    if (card.getStatus().equals("frozen")) {
                        account.addTransactionHistory(TransactionFactory
                                .createErrorTransaction(timestamp,
                                        "The card is frozen"));
                        return false;
                    }
                    return true;
                }
            }
        }
        error = "Card not found";
        return false;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean process() {
        for (BankAccount account : GlobalManager.getGlobal().getBank().getAccounts()) {
            for (Card card : account.getCards()) {
                if (card.getCardNumber().equals(cardNumber)) {
                    User user = GlobalManager.getGlobal()
                            .getBank()
                            .getUserEmail(email);

                    double amountInAccountCurrency;
                    try {
                        amountInAccountCurrency = CurrencyConverter.getConverter()
                                .convert(currency, account.getCurrency(), amount);
                    } catch (IllegalArgumentException e) {
                        return false;
                    }

                    double commission = user.getServicePlan()
                            .calculateCommission(amountInAccountCurrency,
                                    account.getCurrency());

                    if (account.getBalance() < amountInAccountCurrency + commission) {
                        account.addTransactionHistory(TransactionFactory
                                .createErrorTransaction(timestamp,
                                        "Insufficient funds"));
                        return false;
                    }

                    account.payAmount(amountInAccountCurrency + commission);
                    account.checkLargePaymentAndUpgrade(amountInAccountCurrency,
                            account.getCurrency(), timestamp);

                    processCashback(account, user, amountInAccountCurrency);

                    account.addTransactionHistory(TransactionFactory
                            .createOnlineTransaction(timestamp,
                                    "Card payment",
                                    cardNumber,
                                    amountInAccountCurrency,
                                    account.getCurrency(),
                                    commerciant));

                    if (card.getUse() == 0) {
                        handleOneTimeCard(account, card);
                    }
                    return true;
                }
            }
        }
        error = "Card not found";
        return false;
    }

    /**
     *
     * @param account
     * @param user
     * @param amountInAccountCurrency
     */
    private void processCashback(final BankAccount account,
                                 final User user,
                                 final double amountInAccountCurrency) {
        Commerciant comm = GlobalManager.getGlobal()
                .getBank()
                .getCommerciant(commerciant);
        if (comm != null) {
            double amountInRON = amountInAccountCurrency
                    * CurrencyConverter.getConverter()
                    .convert(account.getCurrency(), "RON", 1.0);

            if (comm.getCashbackStrategy().equals("spendingThreshold")) {
                account.addSpendingThresholdTotal(comm.getCommerciant(),
                        amountInRON);
            }

            double cashback = account.processCashback(
                    comm.getType(),
                    commerciant,
                    comm.getCashbackStrategy(),
                    amountInAccountCurrency,
                    user.getServicePlan().getPlanType()
            );

            if (cashback > 0) {
                account.addAmount(cashback);
            }
        }
    }

    /**
     *
     * @param account
     * @param card
     */
    private void handleOneTimeCard(final BankAccount account,
                                   final Card card) {
        account.addTransactionHistory(TransactionFactory
                .createCardTransaction(timestamp,
                        cardNumber,
                        email,
                        account.getIBAN(),
                        false));
        account.deleteCard(card.getCardNumber());

        Card newCard = new Card(email,
                Utils.generateCardNumber(),
                account.getIBAN(),
                timestamp);
        newCard.setUse(0);

        account.addTransactionHistory(TransactionFactory
                .createCardTransaction(timestamp,
                        newCard.getCardNumber(),
                        email,
                        account.getIBAN(),
                        true));
        account.addCard(newCard);
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
