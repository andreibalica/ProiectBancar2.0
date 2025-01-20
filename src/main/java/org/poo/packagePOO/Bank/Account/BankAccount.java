package org.poo.packagePOO.Bank.Account;

import org.poo.packagePOO.Bank.Account.CashBack.CashBackStrategy;
import org.poo.packagePOO.Bank.Account.CashBack.SpendingThresholdCashBack;
import org.poo.packagePOO.Bank.Account.CashBack.TransactionCountCashBack;
import org.poo.packagePOO.Bank.Account.ServicePlan.GoldPlan;
import org.poo.packagePOO.Bank.Account.TransactionsHistory.TransactionFactory;
import org.poo.packagePOO.Bank.Account.TransactionsHistory.TransactionHistory;
import org.poo.packagePOO.Bank.Card;
import org.poo.packagePOO.Bank.User;
import org.poo.packagePOO.CurrencyConverter;
import org.poo.packagePOO.GlobalManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class BankAccount {
    private static final int LARGE_PAYMENT_THRESHOLD = 300;
    private static final int LARGE_PAYMENT_COUNT = 5;

    private final String email;
    private final String iban;
    private final String currency;
    private final String accountType;
    private final int timestamp;
    private double interestRate;
    private double balance = 0;
    private double minBalance = 0;
    private ArrayList<Card> cards = new ArrayList<>();
    private ArrayList<TransactionHistory> transactionHistory = new ArrayList<>();
    private List<Double> largePayments = new ArrayList<>();
    private Map<String, Integer> transactionsPerType = new HashMap<>();
    private double spendingThresholdTotal = 0;
    private Set<String> receivedCashbacks = new HashSet<>();
    private Map<String, Double> spendingThresholdTotals = new HashMap<>();

    /**
     *
     * @param builder
     */
    private BankAccount(final BankAccountBuilder builder) {
        this.email = builder.email;
        this.iban = builder.iban;
        this.currency = builder.currency;
        this.accountType = builder.accountType;
        this.timestamp = builder.timestamp;
        this.interestRate = builder.interestRate;
    }

    public static final class BankAccountBuilder {
        private final String email;
        private final String iban;
        private final String currency;
        private final int timestamp;
        private String accountType;
        private double interestRate;

        /**
         *
         * @param email
         * @param iban
         * @param currency
         * @param timestamp
         */
        public BankAccountBuilder(final String email,
                                  final String iban,
                                  final String currency,
                                  final int timestamp) {
            this.email = email;
            this.iban = iban;
            this.currency = currency;
            this.timestamp = timestamp;
        }

        /**
         *
         * @param accountType
         * @return
         */
        public BankAccountBuilder setAccountType(final String accountType) {
            this.accountType = accountType;
            return this;
        }

        /**
         *
         * @param interestRate
         * @return
         */
        public BankAccountBuilder setInterestRate(final double interestRate) {
            if (!this.accountType.equals("savings")) {
                throw new IllegalArgumentException(
                        "Interest rate is only applicable for savings accounts."
                );
            }
            this.interestRate = interestRate;
            return this;
        }

        /**
         *
         * @return
         */
        public BankAccount build() {
            if (this.accountType == null) {
                throw new IllegalStateException("Account type must be specified!");
            }
            return new BankAccount(this);
        }
    }

    /**
     *
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @return
     */
    public String getIBAN() {
        return iban;
    }

    /**
     *
     * @return
     */
    public String getCurrency() {
        return currency;
    }

    /**
     *
     * @return
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     *
     * @return
     */
    public int getTimestamp() {
        return timestamp;
    }

    /**
     *
     * @return
     */
    public double getInterestRate() {
        return interestRate;
    }

    /**
     *
     * @return
     */
    public double getBalance() {
        return balance;
    }

    /**
     *
     * @return
     */
    public double getMinBalance() {
        return minBalance;
    }

    /**
     *
     * @return
     */
    public ArrayList<Card> getCards() {
        return cards;
    }

    /**
     *
     * @return
     */
    public ArrayList<TransactionHistory> getTransactionHistory() {
        return transactionHistory;
    }

    /**
     *
     * @param amount
     */
    public void addAmount(final double amount) {
        balance += amount;
    }

    /**
     *
     * @param amount
     */
    public void payAmount(final double amount) {
        balance -= amount;
    }

    /**
     *
     * @param card
     */
    public void addCard(final Card card) {
        cards.add(card);
    }

    /**
     *
     * @param card
     */
    public void removeCard(final Card card) {
        cards.remove(card);
    }

    /**
     *
     * @param cardNumber
     */
    public void deleteCard(final String cardNumber) {
        Card card = searchCard(cardNumber);
        if (card != null) {
            cards.remove(card);
        }
    }

    /**
     *
     * @param cardNumber
     * @return
     */
    public Card searchCard(final String cardNumber) {
        for (Card card : cards) {
            if (card.getCardNumber().equals(cardNumber)) {
                return card;
            }
        }
        return null;
    }

    /**
     *
     * @param transaction
     */
    public void addTransactionHistory(final TransactionHistory transaction) {
        transactionHistory.add(transaction);
    }

    /**
     *
     * @param newRate
     */
    public void setInterestRate(final double newRate) {
        if (!accountType.equals("savings")) {
            throw new IllegalStateException("This is not a savings account");
        }
        this.interestRate = newRate;
    }

    /**
     *
     */
    public void applyInterest() {
        if (!accountType.equals("savings")) {
            throw new IllegalStateException("This is not a savings account");
        }
        double interestAmount = balance * interestRate;
        balance += interestAmount;
    }

    /**
     *
     * @param minBalance
     */
    public void setMinBalance(final double minBalance) {
        this.minBalance = minBalance;
    }

    /**
     *
     * @param commerciantType
     * @param commerciantName
     * @param cashbackStrategy
     * @param amount
     * @param userPlanType
     * @return
     */
    public double processCashback(final String commerciantType,
                                  final String commerciantName,
                                  final String cashbackStrategy,
                                  final double amount,
                                  final String userPlanType) {
        double amountInRON = amount;
        if (!currency.equals("RON")) {
            try {
                amountInRON = CurrencyConverter.getConverter()
                        .convert(currency, "RON", amount);
            } catch (IllegalArgumentException e) {
                return 0;
            }
        }

        CashBackStrategy strategy = cashbackStrategy.equals("nrOfTransactions")
                ? new TransactionCountCashBack()
                : new SpendingThresholdCashBack();

        strategy.trackTransaction(this, commerciantType, amountInRON);
        return strategy.calculateCashback(this, commerciantType,
                amount, userPlanType);
    }

    /**
     *
     * @param amount
     * @param currency
     * @param timestamp
     */
    public void checkLargePaymentAndUpgrade(final double amount,
                                            final String currency,
                                            final int timestamp) {
        double amountInRON = amount;
        if (!currency.equals("RON")) {
            try {
                amountInRON = CurrencyConverter.getConverter()
                        .convert(currency, "RON", amount);
            } catch (IllegalArgumentException e) {
                return;
            }
        }

        if (amountInRON >= LARGE_PAYMENT_THRESHOLD) {
            largePayments.add(amountInRON);

            if (largePayments.size() >= LARGE_PAYMENT_COUNT) {
                User user = GlobalManager.getGlobal()
                        .getBank()
                        .getUserEmail(email);

                if (user != null
                        && user.getServicePlan()
                        .getPlanType()
                        .equals("silver")) {
                    user.setServicePlan(new GoldPlan());
                    addTransactionHistory(
                            TransactionFactory.createUpgradePlanTransaction(
                                    timestamp,
                                    iban,
                                    "gold"
                            )
                    );
                }
            }
        }
    }

    /**
     *
     * @param commerciantType
     */
    public void incrementTransactionCount(final String commerciantType) {
        transactionsPerType.merge(commerciantType, 1, Integer::sum);
    }

    /**
     *
     * @param type
     * @return
     */
    public int getTransactionCount(final String type) {
        return transactionsPerType.getOrDefault(type, 0);
    }

    /**
     *
     * @param type
     * @return
     */
    public boolean hasReceivedCashback(final String type) {
        return receivedCashbacks.contains(type);
    }

    /**
     *
     * @param type
     */
    public void markCashbackReceived(final String type) {
        receivedCashbacks.add(type);
    }

    /**
     *
     * @return
     */
    public double getSpendingThresholdTotal() {
        return spendingThresholdTotal;
    }

    /**
     *
     * @param commerciant
     * @return
     */
    public double getSpentForCommerciant(final String commerciant) {
        return spendingThresholdTotals.getOrDefault(commerciant, 0.0);
    }

    /**
     *
     * @param commerciant
     * @param amount
     */
    public void addSpendingThresholdTotal(final String commerciant,
                                          final double amount) {
        spendingThresholdTotals.put(commerciant,
                spendingThresholdTotals.getOrDefault(commerciant, 0.0) + amount);
    }
}
