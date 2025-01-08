package org.poo.packagePOO.Command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.packagePOO.GlobalManager;
import org.poo.packagePOO.Transaction.TransactionStrategy;
import org.poo.packagePOO.Transaction.UpgradePlanStrategy;

public class UpgradePlan implements Command {
    private final TransactionStrategy strategy;
    private final String account;
    private final String newPlanType;
    private final int timestamp;

    public UpgradePlan(String account, String newPlanType, int timestamp) {
        this.account = account;
        this.newPlanType = newPlanType;
        this.timestamp = timestamp;
        this.strategy = new UpgradePlanStrategy(account, newPlanType, timestamp);
    }

    @Override
    public void execute() {
        if ((!strategy.validate() || !strategy.process()) && strategy.getError() != null) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode response = mapper.createObjectNode();
            ObjectNode output = mapper.createObjectNode();

            response.put("command", "upgradePlan");
            output.put("error", strategy.getError());
            output.put("timestamp", timestamp);
            response.set("output", output);
            response.put("timestamp", timestamp);

            GlobalManager.getGlobal().getOutput().add(response);
        }
    }
}
