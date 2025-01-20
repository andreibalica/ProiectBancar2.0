package org.poo.packagePOO.Command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.packagePOO.GlobalManager;
import org.poo.packagePOO.Transaction.AcceptSplitPaymentStrategy;
import org.poo.packagePOO.Transaction.TransactionStrategy;

public class AcceptSplitPayment implements Command {
    private final TransactionStrategy strategy;
    private final String email;
    private final int timestamp;

    /**
     * @param email
     * @param timestamp
     */
    public AcceptSplitPayment(final String email, final int timestamp) {
        this.email = email;
        this.timestamp = timestamp;
        this.strategy = new AcceptSplitPaymentStrategy(email, timestamp);
    }

    /**
     * @return
     */
    @Override
    public void execute() {
        if ((!strategy.validate() || !strategy.process()) && strategy.getError() != null) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode response = mapper.createObjectNode();
            ObjectNode output = mapper.createObjectNode();

            response.put("command", "acceptSplitPayment");
            output.put("timestamp", timestamp);
            output.put("description", strategy.getError());
            response.set("output", output);
            response.put("timestamp", timestamp);

            GlobalManager.getGlobal().getOutput().add(response);
        }
    }
}
