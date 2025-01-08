package org.poo.packagePOO.Command;

import org.poo.packagePOO.Bank.Account.BankAccount;
import org.poo.packagePOO.Bank.Account.TransactionsHistory.TransactionFactory;
import org.poo.packagePOO.Bank.Bank;
import org.poo.packagePOO.Bank.Card;
import org.poo.packagePOO.GlobalManager;
import org.poo.utils.Utils;

public final class CreateCard implements Command {
    private final String email;
    private final String iban;
    private final int timestamp;

    /**
     *
     * @param email
     * @param iban
     * @param timestamp
     */
    public CreateCard(final String email,
                      final String iban,
                      final int timestamp) {
        this.email = email;
        this.iban = iban;
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

        if (account != null) {
            if (!account.getEmail().equals(email)) {
                return;
            }
            String cardNumber = Utils.generateCardNumber();
            Card card = new Card(email, cardNumber, iban, timestamp);
            account.addTransactionHistory(
                    TransactionFactory.createCardTransaction(
                            timestamp,
                            cardNumber,
                            email,
                            iban,
                            true
                    )
            );
            account.addCard(card);
        }
    }
}
