package org.poo.packagePOO.Command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.packagePOO.Bank.Account.BankAccount;
import org.poo.packagePOO.Bank.Account.TransactionsHistory.TransactionHistory;
import org.poo.packagePOO.Bank.Account.TransactionsHistory.TransactionPrinter;
import org.poo.packagePOO.Bank.Account.TransactionsHistory.TransactionVisitor;
import org.poo.packagePOO.GlobalManager;

public final class Report implements Command {
    private final int startTimestamp;
    private final int endTimestamp;
    private final String iban;
    private final int timestamp;

    /**
     *
     * @param startTimestamp
     * @param endTimestamp
     * @param iban
     * @param timestamp
     */
    public Report(final int startTimestamp,
                  final int endTimestamp,
                  final String iban,
                  final int timestamp) {
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
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
        ArrayNode transactionsArray = mapper.createArrayNode();
        BankAccount account = GlobalManager.getGlobal()
                .getBank().getAccountIBAN(iban);

        if (account != null) {
            for (TransactionHistory transaction : account.getTransactionHistory()) {
                if (transaction.getTimestamp() >= startTimestamp
                        && transaction.getTimestamp() <= endTimestamp) {
                    TransactionVisitor visitor = new TransactionPrinter(transactionsArray);
                    transaction.accept(visitor);
                }
            }

            commandNode.put("command", "report");
            outputNode.put("IBAN", iban);
            outputNode.put("balance", account.getBalance());
            outputNode.put("currency", account.getCurrency());
            outputNode.set("transactions", transactionsArray);
            commandNode.set("output", outputNode);
            commandNode.put("timestamp", timestamp);

            GlobalManager.getGlobal().getOutput().add(commandNode);
        } else {
            commandNode.put("command", "report");
            outputNode.put("description", "Account not found");
            outputNode.put("timestamp", timestamp);
            commandNode.set("output", outputNode);
            commandNode.put("timestamp", timestamp);
            GlobalManager.getGlobal().getOutput().add(commandNode);
        }
    }
}
