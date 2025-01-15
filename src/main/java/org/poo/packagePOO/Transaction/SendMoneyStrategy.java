package org.poo.packagePOO.Transaction;

import org.poo.packagePOO.Bank.Account.BankAccount;
import org.poo.packagePOO.Bank.Account.TransactionsHistory.TransactionFactory;
import org.poo.packagePOO.Bank.Commerciant;
import org.poo.packagePOO.Bank.User;
import org.poo.packagePOO.CurrencyConverter;
import org.poo.packagePOO.GlobalManager;

public final class SendMoneyStrategy implements TransactionStrategy {
    private final String sender;
    private final String receiver;
    private final double amount;
    private final String description;
    private final int timestamp;
    private String error;

    public SendMoneyStrategy(final String sender, final double amount,
                             final String receiver, final String description,
                             final int timestamp) {
        this.sender = sender;
        this.amount = amount;
        this.receiver = receiver;
        this.description = description;
        this.timestamp = timestamp;
    }

    @Override
    public boolean validate() {
        if (receiver == null || receiver.trim().isEmpty()) {
            error = "User not found";
            return false;
        }

        BankAccount senderAccount = GlobalManager.getGlobal().getBank()
                .getAccountIBAN(sender);
        if (senderAccount == null) {
            error = "Account not found";
            return false;
        }

        Commerciant commerciant = GlobalManager.getGlobal().getBank()
                .getCommerciant(receiver);
        if (commerciant == null) {
            BankAccount receiverAccount = GlobalManager.getGlobal().getBank()
                    .getAccountIBAN(receiver);
            if (receiverAccount == null) {
                error = "User not found";
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean process() {
        BankAccount senderAccount = GlobalManager.getGlobal().getBank()
                .getAccountIBAN(sender);
        User senderUser = GlobalManager.getGlobal().getBank()
                .getUserEmail(senderAccount.getEmail());

        double commission = senderUser.getServicePlan()
                .calculateCommission(amount, senderAccount.getCurrency());

        Commerciant receiverCommerciant = GlobalManager.getGlobal().getBank()
                .getCommerciant(receiver);
        if (receiverCommerciant != null) {
            double amountInRON = amount * CurrencyConverter.getConverter()
                    .convert(senderAccount.getCurrency(), "RON", 1.0);

            if (senderAccount.getBalance() < amount + commission) {
                senderAccount.addTransactionHistory(
                        TransactionFactory.createErrorTransaction(
                                timestamp, "Insufficient funds"
                        )
                );
                return false;
            }

            senderAccount.payAmount(amount + commission);

            if(receiverCommerciant.getCashbackStrategy().equals("spendingThreshold")) {
                senderAccount.addSpendingThresholdTotal(receiverCommerciant
                        .getCommerciant(), amountInRON);
            }

            double cashback = senderAccount.processCashback(
                    receiverCommerciant.getType(),
                    receiver,
                    receiverCommerciant.getCashbackStrategy(),
                    amount,
                    senderUser.getServicePlan().getPlanType()
            );

            if (cashback > 0) {
                senderAccount.addAmount(cashback);
            }

            senderAccount.addTransactionHistory(
                    TransactionFactory.createTransferTransaction(
                            timestamp,
                            description,
                            sender,
                            receiver,
                            amount,
                            senderAccount.getCurrency(),
                            true
                    )
            );
            return true;
        }

        BankAccount receiverAccount = GlobalManager.getGlobal().getBank()
                .getAccountIBAN(receiver);

        double convertedAmount;
        try {
            convertedAmount = CurrencyConverter.getConverter()
                    .convert(senderAccount.getCurrency(),
                            receiverAccount.getCurrency(),
                            amount);
        } catch (IllegalArgumentException e) {
            return false;
        }


        if (senderAccount.getBalance() < amount + commission) {
            senderAccount.addTransactionHistory(
                    TransactionFactory.createErrorTransaction(
                            timestamp,
                            "Insufficient funds"
                    )
            );
            return false;
        }

        senderAccount.payAmount(amount + commission);
        senderAccount.checkLargePaymentAndUpgrade(amount, senderAccount
                .getCurrency(), timestamp);

        receiverAccount.addAmount(convertedAmount);

        senderAccount.addTransactionHistory(
                TransactionFactory.createTransferTransaction(
                        timestamp,
                        description,
                        sender,
                        receiver,
                        amount,
                        senderAccount.getCurrency(),
                        true
                )
        );

        receiverAccount.addTransactionHistory(
                TransactionFactory.createTransferTransaction(
                        timestamp,
                        description,
                        sender,
                        receiver,
                        convertedAmount,
                        receiverAccount.getCurrency(),
                        false
                )
        );

        return true;
    }

    @Override
    public String getError() {
        return error;
    }
}