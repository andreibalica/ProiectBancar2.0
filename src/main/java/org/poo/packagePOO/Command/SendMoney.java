package org.poo.packagePOO.Command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.packagePOO.GlobalManager;
import org.poo.packagePOO.Transaction.SendMoneyStrategy;

public final class SendMoney implements Command {
    private final SendMoneyStrategy strategy;
    private final String account;
    private final double amount;
    private final String receiver;
    private final String description;
    private final int timestamp;

    /**
     *
     * @param account
     * @param amount
     * @param receiver
     * @param description
     * @param timestamp
     */
    public SendMoney(final String account,
                     final double amount,
                     final String receiver,
                     final String description,
                     final int timestamp) {
        this.account = account;
        this.amount = amount;
        this.receiver = receiver;
        this.description = description;
        this.timestamp = timestamp;
        this.strategy = new SendMoneyStrategy(
                account,
                amount,
                receiver,
                description,
                timestamp
        );
    }

    /**
     *
     * @return
     */
    @Override
    public void execute() {
        if ((!strategy.validate() || !strategy.process())
                && strategy.getError() != null) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode response = mapper.createObjectNode();
            ObjectNode output = mapper.createObjectNode();

            response.put("command", "sendMoney");
            output.put("timestamp", timestamp);
            output.put("description", strategy.getError());
            response.set("output", output);
            response.put("timestamp", timestamp);

            GlobalManager.getGlobal().getOutput().add(response);
        }
    }
}
