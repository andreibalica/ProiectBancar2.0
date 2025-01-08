package org.poo.packagePOO.Command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.packagePOO.Bank.Account.BankAccount;
import org.poo.packagePOO.Bank.Bank;
import org.poo.packagePOO.Bank.Card;
import org.poo.packagePOO.Bank.User;
import org.poo.packagePOO.GlobalManager;

public final class PrintUsers implements Command {
    private final int timestamp;

    /**
     *
     * @param timestamp
     */
    public PrintUsers(final int timestamp) {
        this.timestamp = timestamp;
    }

    /**
     *
     * @return
     */
    @Override
    public void execute() {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode usersArray = mapper.createArrayNode();
        Bank bank = GlobalManager.getGlobal().getBank();

        for (User user : bank.getUsers()) {
            ObjectNode userNode = mapper.createObjectNode();
            userNode.put("firstName", user.getFirstName());
            userNode.put("lastName", user.getLastName());
            userNode.put("email", user.getEmail());

            ArrayNode accountsArray = mapper.createArrayNode();
            for (BankAccount account : bank.getAccounts()) {
                if (account.getEmail().equals(user.getEmail())) {
                    ObjectNode accountNode = mapper.createObjectNode();
                    accountNode.put("IBAN", account.getIBAN());
                    accountNode.put("balance", account.getBalance());
                    accountNode.put("currency", account.getCurrency());
                    accountNode.put("type", account.getAccountType());

                    ArrayNode cardsArray = mapper.createArrayNode();
                    for (Card card : account.getCards()) {
                        ObjectNode cardNode = mapper.createObjectNode();
                        cardNode.put("cardNumber", card.getCardNumber());
                        cardNode.put("status", card.getStatus());
                        cardsArray.add(cardNode);
                    }
                    accountNode.set("cards", cardsArray);
                    accountsArray.add(accountNode);
                }
            }
            userNode.set("accounts", accountsArray);
            usersArray.add(userNode);
        }

        ObjectNode commandNode = mapper.createObjectNode();
        commandNode.put("command", "printUsers");
        commandNode.set("output", usersArray);
        commandNode.put("timestamp", timestamp);
        GlobalManager.getGlobal().getOutput().add(commandNode);
    }
}
