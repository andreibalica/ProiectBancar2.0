package org.poo.packagePOO.Command;

import org.poo.packagePOO.Bank.Account.BankAccount;
import org.poo.packagePOO.GlobalManager;

public final class AddFunds implements Command {
    private final String iban;
    private final double amount;
    private final int timestamp;

    /**
     *
     * @param iban
     * @param amount
     * @param timestamp
     */
    public AddFunds(final String iban,
                    final double amount,
                    final int timestamp) {
        this.iban = iban;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    /**
     *
     * @return
     */
    @Override
    public void execute() {
        BankAccount bankAccount = GlobalManager.getGlobal()
                .getBank().getAccountIBAN(iban);
        if (bankAccount != null) {
            bankAccount.addAmount(amount);
        }
    }
}
