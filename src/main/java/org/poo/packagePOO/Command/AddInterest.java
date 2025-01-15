package org.poo.packagePOO.Command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.packagePOO.Bank.Account.BankAccount;
import org.poo.packagePOO.Bank.Account.TransactionsHistory.TransactionFactory;
import org.poo.packagePOO.GlobalManager;

public final class AddInterest implements Command {
    private final String iban;
    private final int timestamp;

    /**
     *
     * @param iban
     * @param timestamp
     */
    public AddInterest(final String iban,
                       final int timestamp) {
        this.iban = iban;
        this.timestamp = timestamp;
    }

    /**
     *
     * @return
     */
    @Override
    public void execute() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandNode = mapper.createObjectNode();
        ObjectNode outputNode = mapper.createObjectNode();

        BankAccount account = GlobalManager.getGlobal()
                .getBank().getAccountIBAN(iban);

        if (account == null || !account.getAccountType().equals("savings")) {
            outputNode.put("timestamp", timestamp);
            outputNode.put("description", "This is not a savings account");
            commandNode.put("command", "addInterest");
            commandNode.set("output", outputNode);
            commandNode.put("timestamp", timestamp);
            GlobalManager.getGlobal().getOutput().add(commandNode);
        } else {
            double oldBalance = account.getBalance();
            account.applyInterest();
            double interestAmount = account.getBalance() - oldBalance;

            account.addTransactionHistory(
                    TransactionFactory.createInterestTransaction(
                            timestamp,
                            account.getCurrency(),
                            "Interest rate income",
                            interestAmount
                    )
            );
        }
    }
}