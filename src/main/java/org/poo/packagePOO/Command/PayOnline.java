package org.poo.packagePOO.Command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.packagePOO.GlobalManager;
import org.poo.packagePOO.Transaction.PayOnlineStrategy;
import org.poo.packagePOO.Transaction.TransactionStrategy;

public final class PayOnline implements Command {
    private final TransactionStrategy strategy;
    private final String cardNumber;
    private final double amount;
    private final String currency;
    private final String description;
    private final String commerciant;
    private final String email;
    private final int timestamp;

    /**
     *
     * @param cardNumber
     * @param amount
     * @param currency
     * @param description
     * @param commerciant
     * @param email
     * @param timestamp
     */
    public PayOnline(final String cardNumber,
                     final double amount,
                     final String currency,
                     final String description,
                     final String commerciant,
                     final String email,
                     final int timestamp) {
        this.cardNumber = cardNumber;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.commerciant = commerciant;
        this.email = email;
        this.timestamp = timestamp;
        this.strategy = new PayOnlineStrategy(
                cardNumber,
                amount,
                currency,
                description,
                commerciant,
                email,
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

            response.put("command", "payOnline");
            output.put("timestamp", timestamp);
            output.put("description", strategy.getError());
            response.set("output", output);
            response.put("timestamp", timestamp);

            GlobalManager.getGlobal().getOutput().add(response);
        }
    }
}
