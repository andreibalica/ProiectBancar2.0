package org.poo.packagePOO.Command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.packagePOO.Bank.Account.BankAccount;
import org.poo.packagePOO.Bank.Bank;
import org.poo.packagePOO.GlobalManager;

public final class SetAlias implements Command {
    private final String email;
    private final String alias;
    private final String iban;
    private final int timestamp;

    /**
     *
     * @param email
     * @param alias
     * @param iban
     * @param timestamp
     */
    public SetAlias(final String email,
                    final String alias,
                    final String iban,
                    final int timestamp) {
        this.email = email;
        this.alias = alias;
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

        Bank bank = GlobalManager.getGlobal().getBank();
        BankAccount account = bank.getAccountIBAN(iban);

        if (account == null) {
            outputNode.put("error", "Account not found");
            outputNode.put("timestamp", timestamp);
            commandNode.put("command", "setAlias");
            commandNode.set("output", outputNode);
            GlobalManager.getGlobal().getOutput().add(commandNode);
            return;
        }

        bank.addAlias(email, alias, iban);
    }
}
