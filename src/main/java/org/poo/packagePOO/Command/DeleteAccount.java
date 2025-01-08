package org.poo.packagePOO.Command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.packagePOO.Bank.Account.BankAccount;
import org.poo.packagePOO.Bank.Account.TransactionsHistory.TransactionFactory;
import org.poo.packagePOO.Bank.Bank;
import org.poo.packagePOO.Bank.Card;
import org.poo.packagePOO.GlobalManager;

import java.util.ArrayList;

public final class DeleteAccount implements Command {
    private final String email;
    private final String iban;
    private final int timestamp;

    /**
     *
     * @param email
     * @param iban
     * @param timestamp
     */
    public DeleteAccount(final String email,
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
        ObjectMapper mapper = new ObjectMapper();
        BankAccount account = bank.getAccountIBAN(iban);

        if (account != null && account.getEmail().equals(email)) {
            ObjectNode commandNode = mapper.createObjectNode();
            ObjectNode outputNode = mapper.createObjectNode();
            commandNode.put("command", "deleteAccount");

            if (account.getBalance() == 0) {
                outputNode.put("success", "Account deleted");
                outputNode.put("timestamp", timestamp);

                ArrayList<Card> cards = account.getCards();
                while (!cards.isEmpty()) {
                    account.removeCard(cards.getFirst());
                }
                bank.removeAccount(account);
            } else {
                outputNode.put("error",
                        "Account couldn't be deleted - see org.poo.transactions for details");
                outputNode.put("timestamp", timestamp);
                account.addTransactionHistory(
                        TransactionFactory.createErrorTransaction(
                                timestamp,
                                "Account couldn't be deleted - there are funds remaining"
                        )
                );
            }

            commandNode.set("output", outputNode);
            commandNode.put("timestamp", timestamp);
            GlobalManager.getGlobal().getOutput().add(commandNode);
        }
    }
}
