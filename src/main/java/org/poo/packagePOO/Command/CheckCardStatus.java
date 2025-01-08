package org.poo.packagePOO.Command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.packagePOO.Bank.Account.BankAccount;
import org.poo.packagePOO.Bank.Account.TransactionsHistory.TransactionFactory;
import org.poo.packagePOO.Bank.Bank;
import org.poo.packagePOO.Bank.Card;
import org.poo.packagePOO.GlobalManager;

import java.util.ArrayList;

public final class CheckCardStatus implements Command {
    private final String cardNumber;
    private final int timestamp;
    private String status;

    /**
     *
     * @param cardNumber
     * @param timestamp
     */
    public CheckCardStatus(final String cardNumber,
                           final int timestamp) {
        this.cardNumber = cardNumber;
        this.timestamp = timestamp;
        this.status = null;
    }

    /**
     *
     * @return
     */
    @Override
    public void execute() {
        Bank bank = GlobalManager.getGlobal().getBank();
        BankAccount account = bank.getAccountCard(cardNumber);
        if (account != null) {
            Card card = account.searchCard(cardNumber);
            if (card != null) {
                double currentBalance = account.getBalance();
                double minBalance = account.getMinBalance();
                double difference = currentBalance - minBalance;
                if (currentBalance <= minBalance) {
                    if (!card.getStatus().equals("frozen")) {
                        status = "frozen";
                        account.addTransactionHistory(
                                TransactionFactory.createErrorTransaction(
                                        timestamp,
                                        "You have reached the minimum amount of funds,"
                                                + " the card will be frozen"
                                )
                        );
                    } else {
                        account.addTransactionHistory(
                                TransactionFactory.createErrorTransaction(
                                        timestamp,
                                        "The card is frozen"
                                )
                        );
                    }
                } else if (difference <= 30) {
                    status = "warning";
                }
                if (status != null) {
                    ArrayList<Card> cards = account.getCards();
                    for (Card cardIndex : cards) {
                        cardIndex.setStatus(status);
                    }
                }
            }
        }
        if (account == null || account.searchCard(cardNumber) == null) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode commandNode = mapper.createObjectNode();
            ObjectNode outputNode = mapper.createObjectNode();

            commandNode.put("command", "checkCardStatus");
            outputNode.put("description", "Card not found");
            outputNode.put("timestamp", timestamp);
            commandNode.set("output", outputNode);
            commandNode.put("timestamp", timestamp);

            GlobalManager.getGlobal().getOutput().add(commandNode);
        }
    }
}
