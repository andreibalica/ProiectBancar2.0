package org.poo.packagePOO.Bank.Account.TransactionsHistory;

import java.util.ArrayList;

public final class TransactionFactory {
    private TransactionFactory() {
    }
    /**
     *
     * @param timestamp
     * @return
     */
    public static TransactionHistory createAccountCreationTransaction(final int timestamp) {
        return new CreateAccountTransaction(timestamp, "New account created");
    }

    /**
     *
     * @param timestamp
     * @param card
     * @param cardHolder
     * @param account
     * @param isCreation
     * @return
     */
    public static TransactionHistory createCardTransaction(final int timestamp,
                                                           final String card,
                                                           final String cardHolder,
                                                           final String account,
                                                           final boolean isCreation) {
        String description = isCreation ? "New card created" : "The card has been destroyed";
        return new CardCreateDeleteTransaction(timestamp, description,
                card, cardHolder, account);
    }

    /**
     *
     * @param timestamp
     * @param description
     * @param senderIBAN
     * @param receiverIBAN
     * @param amount
     * @param currency
     * @param isSender
     * @return
     */
    public static TransactionHistory createTransferTransaction(final int timestamp,
                                                               final String description,
                                                               final String senderIBAN,
                                                               final String receiverIBAN,
                                                               final double amount,
                                                               final String currency,
                                                               final boolean isSender) {
        String transferType = isSender ? "sent" : "received";
        return new MoneyTransferTransaction(timestamp, description,
                senderIBAN, receiverIBAN, amount, currency, transferType);
    }

    /**
     *
     * @param timestamp
     * @param description
     * @param cardNumber
     * @param amount
     * @param currency
     * @param commerciant
     * @return
     */
    public static TransactionHistory createOnlineTransaction(final int timestamp,
                                                             final String description,
                                                             final String cardNumber,
                                                             final double amount,
                                                             final String currency,
                                                             final String commerciant) {
        return new CardPaymentTransaction(timestamp, description,
                cardNumber, amount, currency, commerciant);
    }

    /**
     *
     * @param timestamp
     * @param errorMessage
     * @return
     */
    public static TransactionHistory createErrorTransaction(final int timestamp,
                                                            final String errorMessage) {
        return new ErrorTransaction(timestamp, errorMessage);
    }

    /**
     *
     * @param timestamp
     * @param totalAmount
     * @param currency
     * @param accounts
     * @param error
     * @return
     */
    public static TransactionHistory createSplitPaymentTransaction(final int timestamp,
                                                                   final double totalAmount,
                                                                   final String currency,
                                                                   final ArrayList<String> accounts,
                                                                   final String error,
                                                                   final String splitPaymentType,
                                                                   final ArrayList<Double> amountForUsers) {
        return new SplitPaymentsTransaction(timestamp,
                totalAmount,
                currency,
                accounts,
                error,
                splitPaymentType,
                amountForUsers);
    }

    /**
     *
     * @param timestamp
     * @param description
     * @param amount
     * @return
     */
    public static TransactionHistory createInterestTransaction(final int timestamp,
                                                               final String currency,
                                                               final String description,
                                                               final double amount) {
        return new InterestTransaction(timestamp, currency, description, amount);
    }


    /**
     *
     * @param timestamp
     * @param description
     * @param newRate
     * @return
     */
    public static TransactionHistory createInterestRateTransaction(final int timestamp,
                                                                   final String description,
                                                                   final double newRate) {
        return new InterestRateTransaction(timestamp, description, newRate);
    }

    /**
     *
     * @param timestamp
     * @param account
     * @param amount
     * @param currency
     * @param error
     * @return
     */
    public static TransactionHistory createWithdrawSavingsTransaction(final int timestamp,
                                                                      final String account,
                                                                      final double amount,
                                                                      final String currency,
                                                                      final String error) {
        return new WithdrawSavingsTransaction(timestamp, account, amount, currency, error);
    }

    /**
     *
     * @param timestamp
     * @param accountIBAN
     * @param newPlanType
     * @return
     */
    public static TransactionHistory createUpgradePlanTransaction(
            final int timestamp,
            final String accountIBAN,
            final String newPlanType) {
        String description = "Upgrade plan";
        return new UpgradePlanTransaction(timestamp, description, accountIBAN, newPlanType);
    }

    /**
     *
     * @param timestamp
     * @param amount
     * @return
     */
    public static TransactionHistory createCashWithdrawalTransaction(final int timestamp,
                                                                     final double amount) {
        return new CashWithdrawalTransaction(timestamp, amount);
    }
}
