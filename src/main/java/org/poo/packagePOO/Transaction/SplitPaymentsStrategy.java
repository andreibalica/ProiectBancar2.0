package org.poo.packagePOO.Transaction;

import org.poo.packagePOO.Bank.Account.BankAccount;
import org.poo.packagePOO.Bank.SplitPaymentRequest;
import org.poo.packagePOO.GlobalManager;
import java.util.ArrayList;

public final class SplitPaymentsStrategy implements TransactionStrategy {
    private final ArrayList<String> accounts;
    private final double totalAmount;
    private final String currency;
    private final int timestamp;
    private final ArrayList<Double> amountForUsers;
    private final String splitPaymentType;
    private String error;

    /**
     *
     * @param accounts
     * @param totalAmount
     * @param currency
     * @param timestamp
     * @param amountForUsers
     * @param splitPaymentType
     */
    public SplitPaymentsStrategy(final ArrayList<String> accounts,
                                 final double totalAmount,
                                 final String currency,
                                 final int timestamp,
                                 final ArrayList<Double> amountForUsers,
                                 final String splitPaymentType) {
        this.accounts = accounts;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.timestamp = timestamp;
        this.amountForUsers = amountForUsers;
        this.splitPaymentType = splitPaymentType;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean validate() {
        for (String accountIBAN : accounts) {
            BankAccount account = GlobalManager.getGlobal()
                    .getBank()
                    .getAccountIBAN(accountIBAN);
            if (account == null) {
                error = "One of the accounts is invalid";
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean process() {
        SplitPaymentRequest request = new SplitPaymentRequest(
                new ArrayList<>(accounts),
                totalAmount,
                currency,
                amountForUsers != null ? new ArrayList<>(amountForUsers) : null,
                splitPaymentType,
                timestamp
        );

        GlobalManager.getGlobal()
                .getBank()
                .addPendingSplitPayment(request);
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
