package org.poo.packagePOO.Bank;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public final class SplitPaymentRequest {
    private final ArrayList<String> accounts;
    private final double amount;
    private final String currency;
    private final ArrayList<Double> amountForUsers;
    private final String splitPaymentType;
    private final Set<String> acceptedBy;
    private final int timestamp;
    private boolean isRejected;

    /**
     *
     * @param accounts
     * @param amount
     * @param currency
     * @param amountForUsers
     * @param splitPaymentType
     * @param timestamp
     */
    public SplitPaymentRequest(final ArrayList<String> accounts,
                               final double amount,
                               final String currency,
                               final ArrayList<Double> amountForUsers,
                               final String splitPaymentType,
                               final int timestamp) {
        this.accounts = accounts;
        this.amount = amount;
        this.currency = currency;
        this.amountForUsers = amountForUsers;
        this.splitPaymentType = splitPaymentType;
        this.timestamp = timestamp;
        this.acceptedBy = new HashSet<>();
        this.isRejected = false;
    }

    /**
     *
     * @param email
     */
    public void addAcceptance(final String email) {
        acceptedBy.add(email);
    }

    /**
     *
     * @return
     */
    public boolean isFullyAccepted() {
        return acceptedBy.size() == accounts.size();
    }

    /**
     *
     */
    public void reject() {
        this.isRejected = true;
    }

    /**
     *
     * @return
     */
    public boolean isRejected() {
        return isRejected;
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getAccounts() {
        return accounts;
    }

    /**
     *
     * @return
     */
    public double getAmount() {
        return amount;
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
    public ArrayList<Double> getAmountForUsers() {
        return amountForUsers;
    }

    /**
     *
     * @return
     */
    public String getSplitPaymentType() {
        return splitPaymentType;
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
    public int getAcceptanceCount() {
        return acceptedBy.size();
    }
}