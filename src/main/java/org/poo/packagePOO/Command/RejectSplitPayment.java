package org.poo.packagePOO.Command;

import org.poo.packagePOO.Bank.Account.BankAccount;
import org.poo.packagePOO.Bank.Account.TransactionsHistory.TransactionFactory;
import org.poo.packagePOO.GlobalManager;

public class RejectSplitPayment implements Command {
    private final String email;
    private final int timestamp;

    public RejectSplitPayment(final String email, final int timestamp) {
        this.email = email;
        this.timestamp = timestamp;
    }

    @Override
    public void execute() {
    }
}