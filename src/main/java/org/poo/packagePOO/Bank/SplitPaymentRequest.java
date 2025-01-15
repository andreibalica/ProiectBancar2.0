package org.poo.packagePOO.Bank;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SplitPaymentRequest {
    private final ArrayList<String> accounts;
    private final double amount;
    private final String currency;
    private final ArrayList<Double> amountForUsers;
    private final String splitPaymentType;
    private final Set<String> acceptedBy;
    private final int timestamp;
    private boolean isRejected;

    public SplitPaymentRequest(ArrayList<String> accounts, double amount,
                               String currency, ArrayList<Double> amountForUsers,
                               String splitPaymentType, int timestamp) {
        this.accounts = accounts;
        this.amount = amount;
        this.currency = currency;
        this.amountForUsers = amountForUsers;
        this.splitPaymentType = splitPaymentType;
        this.timestamp = timestamp;
        this.acceptedBy = new HashSet<>();
        this.isRejected = false;
    }

    public void addAcceptance(String email) {
        acceptedBy.add(email);
    }

    public boolean isFullyAccepted() {
        return acceptedBy.size() == accounts.size();
    }

    public void reject() {
        this.isRejected = true;
    }

    public boolean isRejected() {
        return isRejected;
    }

    public ArrayList<String> getAccounts() { return accounts; }
    public double getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public ArrayList<Double> getAmountForUsers() { return amountForUsers; }
    public String getSplitPaymentType() { return splitPaymentType; }
    public int getTimestamp() { return timestamp; }
    public int getAcceptanceCount() { return acceptedBy.size(); }
}