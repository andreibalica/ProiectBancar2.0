package org.poo.packagePOO.Command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.packagePOO.Bank.Account.BankAccount;
import org.poo.packagePOO.Bank.Account.TransactionsHistory.TransactionFactory;
import org.poo.packagePOO.GlobalManager;

public final class ChangeInterestRate implements Command {
    private final String iban;
    private final double newRate;
    private final int timestamp;

    /**
     *
     * @param iban
     * @param newRate
     * @param timestamp
     */
    public ChangeInterestRate(final String iban,
                              final double newRate,
                              final int timestamp) {
        this.iban = iban;
        this.newRate = newRate;
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
            commandNode.put("command", "changeInterestRate");
            commandNode.set("output", outputNode);
            commandNode.put("timestamp", timestamp);
            GlobalManager.getGlobal().getOutput().add(commandNode);
        } else {
            account.setInterestRate(newRate);
            account.addTransactionHistory(
                    TransactionFactory.createInterestRateTransaction(
                            timestamp,
                            "Interest rate of the account changed to " + newRate,
                            newRate
                    )
            );
            commandNode.put("timestamp", timestamp);
        }
    }
}
