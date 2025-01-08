package org.poo.packagePOO.Command;

import org.poo.packagePOO.Bank.Account.BankAccount;
import org.poo.packagePOO.Bank.Bank;
import org.poo.packagePOO.GlobalManager;

public final class SetMinimumBalance implements Command {
    private final String iban;
    private final double minBalance;
    private final int timestamp;

    /**
     *
     * @param iban
     * @param minBalance
     * @param timestamp
     */
    public SetMinimumBalance(final String iban,
                             final double minBalance,
                             final int timestamp) {
        this.iban = iban;
        this.minBalance = minBalance;
        this.timestamp = timestamp;
    }

    /**
     *
     * @return
     */
    @Override
    public void execute() {
        Bank bank = GlobalManager.getGlobal().getBank();
        BankAccount account = bank.getAccountIBAN(iban);
        account.setMinBalance(minBalance);
    }
}
