package org.poo.packagePOO.Command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.packagePOO.GlobalManager;
import org.poo.packagePOO.Transaction.SplitPaymentsStrategy;

import java.util.ArrayList;

public final class SplitPayments implements Command {
    private final SplitPaymentsStrategy strategy;
    private final ArrayList<String> accounts;
    private final int timestamp;
    private final String currency;
    private final double amount;
    private final ArrayList<Double> amountForUsers;
    private final String splitPaymentType;

    public SplitPayments(final ArrayList<String> accounts,
                         final int timestamp,
                         final String currency,
                         final double amount,
                         final ArrayList<Double> amountForUsers,
                         final String splitPaymentType) {
        this.accounts = accounts;
        this.timestamp = timestamp;
        this.currency = currency;
        this.amount = amount;
        this.amountForUsers = amountForUsers;
        this.splitPaymentType = splitPaymentType;
        strategy = new SplitPaymentsStrategy(
                accounts,
                amount,
                currency,
                timestamp,
                amountForUsers,
                splitPaymentType
        );
    }

    @Override
    public void execute() {
        if ((!strategy.validate() || !strategy.process())
                && strategy.getError() != null) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode response = mapper.createObjectNode();
            ObjectNode output = mapper.createObjectNode();

            response.put("command", "splitPayments");
            output.put("timestamp", timestamp);
            output.put("description", strategy.getError());
            response.set("output", output);
            response.put("timestamp", timestamp);

            GlobalManager.getGlobal().getOutput().add(response);
        }
    }
}