package org.poo.packagePOO.Transaction;

import org.poo.packagePOO.Bank.Account.BankAccount;
import org.poo.packagePOO.Bank.Account.TransactionsHistory.TransactionFactory;
import org.poo.packagePOO.Bank.SplitPaymentRequest;
import org.poo.packagePOO.Bank.User;
import org.poo.packagePOO.CurrencyConverter;
import org.poo.packagePOO.GlobalManager;

public final class AcceptSplitPaymentStrategy implements TransactionStrategy {
    private final String email;
    private final int timestamp;
    private String error;

    public AcceptSplitPaymentStrategy(String email, int timestamp) {
        this.email = email;
        this.timestamp = timestamp;
    }

    @Override
    public boolean validate() {
        SplitPaymentRequest request = GlobalManager.getGlobal().getBank()
                .getNextPendingSplitPayment(email);

        if (request == null) {
            error = "No pending split payment";
            return false;
        }

        if (request.isRejected()) {
            error = "One user rejected the payment.";
            return false;
        }



        return true;
    }

    @Override
    public boolean process() {
        SplitPaymentRequest request = GlobalManager.getGlobal().getBank()
                .getNextPendingSplitPayment(email);
        if (request == null) {
            return false;
        }

        request.addAcceptance(email);

        if (request.isFullyAccepted()) {
            BankAccount insufficientAccount = null;
            for (int i = 0; i < request.getAccounts().size(); i++) {
                String accountIBAN = request.getAccounts().get(i);
                BankAccount acc = GlobalManager.getGlobal().getBank().getAccountIBAN(accountIBAN);
                if (acc != null) {
                    double amount;
                    if ("equal".equals(request.getSplitPaymentType())) {
                        amount = request.getAmount() / request.getAccounts().size();
                    } else {
                        amount = request.getAmountForUsers().get(i);
                    }

                    try {
                        double convertedAmount = CurrencyConverter.getConverter()
                                .convert(request.getCurrency(), acc.getCurrency(), amount);
                        User user = GlobalManager.getGlobal().getBank().getUserEmail(acc.getEmail());
                        if (user != null) {
                            double commission = user.getServicePlan()
                                    .calculateCommission(convertedAmount, acc.getCurrency());
                            convertedAmount += commission;
                        }

                        if (acc.getBalance() < convertedAmount) {
                            insufficientAccount = acc;
                            break;
                        }
                    } catch (IllegalArgumentException e) {
                        continue;
                    }
                }
            }

            if (insufficientAccount != null) {
                for (String accountIBAN : request.getAccounts()) {
                    BankAccount acc = GlobalManager.getGlobal().getBank().getAccountIBAN(accountIBAN);
                    if (acc != null) {
                        acc.addTransactionHistory(
                                TransactionFactory.createSplitPaymentTransaction(
                                        request.getTimestamp(),
                                        request.getAmount(),
                                        request.getCurrency(),
                                        request.getAccounts(),
                                        "Account " + insufficientAccount.getIBAN() +
                                                " has insufficient funds for a split payment.",
                                        request.getSplitPaymentType(),
                                        request.getSplitPaymentType().equals("equal") ? null :
                                                request.getAmountForUsers()
                                )
                        );
                        GlobalManager.getGlobal().getBank()
                                .removePendingSplitPayment(acc.getEmail());
                    }
                }
                return false;
            }

            processPayments(request);
        }

        return true;
    }

    private void processPayments(SplitPaymentRequest request) {
        for (int i = 0; i < request.getAccounts().size(); i++) {
            String accountIBAN = request.getAccounts().get(i);
            BankAccount acc = GlobalManager.getGlobal().getBank().getAccountIBAN(accountIBAN);
            if (acc != null) {
                double amount;
                if ("equal".equals(request.getSplitPaymentType())) {
                    amount = request.getAmount() / request.getAccounts().size();
                } else {
                    amount = request.getAmountForUsers().get(i);
                }

                try {
                    double convertedAmount = CurrencyConverter.getConverter()
                            .convert(request.getCurrency(), acc.getCurrency(), amount);
                    User user = GlobalManager.getGlobal().getBank().getUserEmail(acc.getEmail());
                    if (user != null) {
                        double commission = user.getServicePlan()
                                .calculateCommission(convertedAmount, acc.getCurrency());
                        convertedAmount += commission;
                    }
                    acc.payAmount(convertedAmount);
                } catch (IllegalArgumentException e) {
                    continue;
                }
            }
        }

        for (String accountIBAN : request.getAccounts()) {
            BankAccount acc = GlobalManager.getGlobal().getBank().getAccountIBAN(accountIBAN);
            if (acc != null) {
                acc.addTransactionHistory(
                        TransactionFactory.createSplitPaymentTransaction(
                                request.getTimestamp(),
                                request.getAmount(),
                                request.getCurrency(),
                                request.getAccounts(),
                                null,
                                request.getSplitPaymentType(),
                                request.getSplitPaymentType().equals("equal") ? null :
                                        request.getAmountForUsers()
                        )
                );
                GlobalManager.getGlobal().getBank()
                        .removePendingSplitPayment(acc.getEmail());
            }
        }
    }

    @Override
    public String getError() {
        return error;
    }
}