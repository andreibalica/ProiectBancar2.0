package org.poo.packagePOO.Command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.packagePOO.GlobalManager;
import org.poo.packagePOO.Transaction.CashWithdrawalStrategy;
import org.poo.packagePOO.Transaction.TransactionStrategy;

public class CashWithdrawal implements Command {
    private final String cardNumber;
    private final double amount;
    private final String email;
    private final String location;
    private final int timestamp;
    private final TransactionStrategy strategy;

    /**
     * @param cardNumber
     * @param amount
     * @param email
     * @param location
     * @param timestamp
     */
    public CashWithdrawal(final String cardNumber,
                          final double amount,
                          final String email,
                          final String location,
                          final int timestamp) {
        this.cardNumber = cardNumber;
        this.amount = amount;
        this.email = email;
        this.location = location;
        this.timestamp = timestamp;
        this.strategy = new CashWithdrawalStrategy(cardNumber, amount, email, location, timestamp);
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

            response.put("command", "cashWithdrawal");
            output.put("timestamp", timestamp);
            output.put("description", strategy.getError());
            response.set("output", output);
            response.put("timestamp", timestamp);

            GlobalManager.getGlobal().getOutput().add(response);
        }
    }
}
