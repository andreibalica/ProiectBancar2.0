package org.poo.packagePOO.Command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.packagePOO.GlobalManager;
import org.poo.packagePOO.Transaction.TransactionStrategy;
import org.poo.packagePOO.Transaction.WithdrawSavingsStrategy;

public class WithdrawSavings implements Command {
    private final String account;
    private final double amount;
    private final String currency;
    private final int timestamp;
    private final TransactionStrategy strategy;;

    public WithdrawSavings(final String account,
                           final double amount,
                           final String currency,
                           final int timestamp) {
        this.account = account;
        this.amount = amount;
        this.currency = currency;
        this.strategy = new WithdrawSavingsStrategy(account, amount, currency, timestamp);
        this.timestamp = timestamp;
    }

    @Override
    public void execute() {
        if ((!strategy.validate() || !strategy.process()) && strategy.getError() != null) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode response = mapper.createObjectNode();
            ObjectNode output = mapper.createObjectNode();

            response.put("command", "withdrawSavings");
            output.put("timestamp", timestamp);
            output.put("description", strategy.getError());
            response.set("output", output);
            response.put("timestamp", timestamp);

            GlobalManager.getGlobal().getOutput().add(response);
        }
    }
}
