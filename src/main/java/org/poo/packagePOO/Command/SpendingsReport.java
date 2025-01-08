package org.poo.packagePOO.Command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.packagePOO.Bank.Account.BankAccount;
import org.poo.packagePOO.Bank.Account.TransactionsHistory.CardPaymentTransaction;
import org.poo.packagePOO.Bank.Account.TransactionsHistory.TransactionHistory;
import org.poo.packagePOO.GlobalManager;

import java.util.HashMap;
import java.util.Map;

public final class SpendingsReport implements Command {
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
    public SpendingsReport(final int startTimestamp,
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
        ArrayNode transactions = mapper.createArrayNode();
        ArrayNode commerciants = mapper.createArrayNode();
        Map<String, Double> totals = new HashMap<>();

        BankAccount account = GlobalManager.getGlobal()
                .getBank().getAccountIBAN(iban);
        if (account != null) {
            if (account.getAccountType().equals("savings")) {
                outputNode.put("error",
                        "This kind of report is not supported for a saving account");
                commandNode.put("command", "spendingsReport");
                commandNode.set("output", outputNode);
                commandNode.put("timestamp", timestamp);
                GlobalManager.getGlobal().getOutput().add(commandNode);
                return;
            }

            for (TransactionHistory transaction : account.getTransactionHistory()) {
                if (transaction.getTimestamp() >= startTimestamp
                        && transaction.getTimestamp() <= endTimestamp
                        && transaction.isPayment()) {
                    CardPaymentTransaction payment = (CardPaymentTransaction) transaction;

                    ObjectNode transactionNode = mapper.createObjectNode();
                    transactionNode.put("timestamp", payment.getTimestamp());
                    transactionNode.put("description", payment.getDescription());
                    transactionNode.put("amount", payment.getAmount());
                    transactionNode.put("commerciant", payment.getCommerciant());
                    transactions.add(transactionNode);

                    totals.merge(payment.getCommerciant(),
                            payment.getAmount(), Double::sum);
                }
            }

            totals.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> {
                        ObjectNode node = mapper.createObjectNode();
                        node.put("commerciant", entry.getKey());
                        node.put("total", entry.getValue());
                        commerciants.add(node);
                    });

            commandNode.put("command", "spendingsReport");
            outputNode.put("IBAN", iban);
            outputNode.put("balance", account.getBalance());
            outputNode.put("currency", account.getCurrency());
            outputNode.set("transactions", transactions);
            outputNode.set("commerciants", commerciants);
            commandNode.set("output", outputNode);
            commandNode.put("timestamp", timestamp);

            GlobalManager.getGlobal().getOutput().add(commandNode);
        } else {
            commandNode.put("command", "spendingsReport");
            outputNode.put("description", "Account not found");
            outputNode.put("timestamp", timestamp);
            commandNode.set("output", outputNode);
            commandNode.put("timestamp", timestamp);
            GlobalManager.getGlobal().getOutput().add(commandNode);
        }
    }
}
