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

import java.util.*;

public final class BankAccount {
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

    public void incrementTransactionCount(String commerciantType) {
        transactionsPerType.merge(commerciantType, 1, Integer::sum);
    }

    public int getTransactionCount(String type) {
        return transactionsPerType.getOrDefault(type, 0);
    }

    public boolean hasReceivedCashback(String type) {
        return receivedCashbacks.contains(type);
    }

    public void markCashbackReceived(String type) {
        receivedCashbacks.add(type);
    }

    public double getSpendingThresholdTotal() {
        return spendingThresholdTotal;
    }

    public void addToSpendingThresholdTotal(double amount) {
        spendingThresholdTotal += amount;
    }

    public double getSpentForMerchant(String merchant) {
        return spendingThresholdTotals.getOrDefault(merchant, 0.0);
    }

    public void addSpendingThresholdTotal(String commerciant, double amount) {
        spendingThresholdTotals.put(commerciant,
                spendingThresholdTotals.getOrDefault(commerciant, 0.0) + amount);
    }

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

        public BankAccountBuilder(final String email, final String iban,
                                  final String currency, final int timestamp) {
            this.email = email;
            this.iban = iban;
            this.currency = currency;
            this.timestamp = timestamp;
        }

        public BankAccountBuilder setAccountType(final String accountType) {
            this.accountType = accountType;
            return this;
        }

        public BankAccountBuilder setInterestRate(final double interestRate) {
            if (!this.accountType.equals("savings")) {
                throw new IllegalArgumentException(
                        "Interest rate is only applicable for savings accounts."
                );
            }
            this.interestRate = interestRate;
            return this;
        }

        public BankAccount build() {
            if (this.accountType == null) {
                throw new IllegalStateException("Account type must be specified!");
            }
            return new BankAccount(this);
        }
    }

    public String getEmail() { return email; }
    public String getIBAN() { return iban; }
    public String getCurrency() { return currency; }
    public String getAccountType() { return accountType; }
    public int getTimestamp() { return timestamp; }
    public double getInterestRate() { return interestRate; }
    public double getBalance() { return balance; }
    public double getMinBalance() { return minBalance; }
    public ArrayList<Card> getCards() { return cards; }
    public ArrayList<TransactionHistory> getTransactionHistory() {
        return transactionHistory;
    }

    public void addAmount(final double amount) {
        balance += amount;
        balance = Math.round(balance * 100.0) / 100.0;
    }

    public void payAmount(final double amount) {
        balance -= amount;
        balance = Math.round(balance * 100.0) / 100.0;
    }

    public void addCard(final Card card) {
        cards.add(card);
    }

    public void removeCard(final Card card) {
        cards.remove(card);
    }

    public void deleteCard(final String cardNumber) {
        Card card = searchCard(cardNumber);
        if (card != null) {
            cards.remove(card);
        }
    }

    public Card searchCard(final String cardNumber) {
        for (Card card : cards) {
            if (card.getCardNumber().equals(cardNumber)) {
                return card;
            }
        }
        return null;
    }

    public void addTransactionHistory(final TransactionHistory transaction) {
        transactionHistory.add(transaction);
    }

    public void setInterestRate(final double newRate) {
        if (!accountType.equals("savings")) {
            throw new IllegalStateException("This is not a savings account");
        }
        this.interestRate = newRate;
    }

    public void applyInterest() {
        if (!accountType.equals("savings")) {
            throw new IllegalStateException("This is not a savings account");
        }
        double interestAmount = balance * (interestRate / 100.0);
        balance += interestAmount;
    }

    public void setMinBalance(final double minBalance) {
        this.minBalance = minBalance;
    }

    public double processCashback(String commerciantType, String commerciantName,
                                  String cashbackStrategy, double amount, String userPlanType) {
        double amountInRON = amount;
        if (!currency.equals("RON")) {
            try {
                amountInRON = CurrencyConverter.getConverter()
                        .convert(currency, "RON", amount);
            } catch (IllegalArgumentException e) {
                return 0;
            }
        }

        CashBackStrategy strategy = cashbackStrategy.equals("nrOfTransactions") ?
                new TransactionCountCashBack() : new SpendingThresholdCashBack();

        strategy.trackTransaction(this, commerciantType, amountInRON);
        return strategy.calculateCashback(this, commerciantType, amount, userPlanType);
    }

    public void checkLargePaymentAndUpgrade(double amount, String currency, int timestamp) {
        double amountInRON = amount;
        if (!currency.equals("RON")) {
            try {
                amountInRON = CurrencyConverter.getConverter().convert(currency, "RON", amount);
            } catch (IllegalArgumentException e) {
                return;
            }
        }

        if (amountInRON >= 300) {
            largePayments.add(amountInRON);

            if (largePayments.size() >= 5) {
                User user = GlobalManager.getGlobal().getBank().getUserEmail(email);
                if (user != null && user.getServicePlan().getPlanType().equals("silver")) {
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
}