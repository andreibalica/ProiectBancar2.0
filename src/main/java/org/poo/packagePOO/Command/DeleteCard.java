package org.poo.packagePOO.Command;

import org.poo.packagePOO.Bank.Account.BankAccount;
import org.poo.packagePOO.Bank.Account.TransactionsHistory.TransactionFactory;
import org.poo.packagePOO.Bank.Bank;
import org.poo.packagePOO.Bank.Card;
import org.poo.packagePOO.GlobalManager;

public final class DeleteCard implements Command {
    private final String cardNumber;
    private final String email;
    private final int timestamp;

    /**
     *
     * @param cardNumber
     * @param email
     * @param timestamp
     */
    public DeleteCard(final String cardNumber,
                      final String email,
                      final int timestamp) {
        this.cardNumber = cardNumber;
        this.email = email;
        this.timestamp = timestamp;
    }

    /**
     *
     * @return
     */
    @Override
    public void execute() {
        Bank bank = GlobalManager.getGlobal().getBank();
        BankAccount account = bank.getAccountCard(cardNumber);
        if (account == null) {
            return;
        }
        Card card = account.searchCard(cardNumber);
        if (card != null && card.getEmail().equals(email)) {
            String accountIban = account.getIBAN();

            account.addTransactionHistory(
                    TransactionFactory.createCardTransaction(
                            timestamp,
                            cardNumber,
                            email,
                            accountIban,
                            false
                    )
            );

            account.deleteCard(cardNumber);
        }
    }
}
