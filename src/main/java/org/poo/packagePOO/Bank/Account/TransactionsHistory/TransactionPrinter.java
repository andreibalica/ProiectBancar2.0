package org.poo.packagePOO.Bank.Account.TransactionsHistory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public final class TransactionPrinter implements TransactionVisitor {
    private final ArrayNode output;

    /**
     *
     * @param output
     */
    public TransactionPrinter(final ArrayNode output) {
        this.output = output;
    }

    /**
     *
     * @param transaction
     */
    @Override
    public void visit(final CreateAccountTransaction transaction) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("timestamp", transaction.getTimestamp());
        node.put("description", transaction.getDescription());
        output.add(node);
    }

    /**
     *
     * @param transaction
     */
    @Override
    public void visit(final CardCreateDeleteTransaction transaction) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("timestamp", transaction.getTimestamp());
        node.put("description", transaction.getDescription());
        node.put("card", transaction.getCard());
        node.put("cardHolder", transaction.getCardHolder());
        node.put("account", transaction.getAccount());
        output.add(node);
    }

    /**
     *
     * @param transaction
     */
    @Override
    public void visit(final MoneyTransferTransaction transaction) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("timestamp", transaction.getTimestamp());
        node.put("description", transaction.getDescription());
        node.put("senderIBAN", transaction.getSenderIBAN());
        node.put("receiverIBAN", transaction.getReceiverIBAN());
        node.put("amount", transaction.getAmount());
        node.put("transferType", transaction.getTransferType());
        output.add(node);
    }

    /**
     *
     * @param transaction
     */
    @Override
    public void visit(final CardPaymentTransaction transaction) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("timestamp", transaction.getTimestamp());
        node.put("description", transaction.getDescription());
        node.put("amount", transaction.getAmount());
        node.put("commerciant", transaction.getCommerciant());
        output.add(node);
    }

    /**
     *
     * @param transaction
     */
    @Override
    public void visit(final ErrorTransaction transaction) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("timestamp", transaction.getTimestamp());
        node.put("description", transaction.getDescription());
        output.add(node);
    }

    /**
     *
     * @param transaction
     */
    @Override
    public void visit(final SplitPaymentsTransaction transaction) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        node.put("timestamp", transaction.getTimestamp());
        node.put("description", transaction.getDescription());

        if (transaction.getSplitPaymentType() != null) {
            node.put("splitPaymentType", transaction.getSplitPaymentType());
        }

        node.put("currency", transaction.getCurrency());

        ArrayNode accountsArray = mapper.createArrayNode();
        for (String account : transaction.getInvolvedAccounts()) {
            accountsArray.add(account);
        }
        node.set("involvedAccounts", accountsArray);

        if (transaction.getAmountForUsers() != null
                && "custom".equals(transaction.getSplitPaymentType())) {
            ArrayNode amountArray = mapper.createArrayNode();
            for (Double amount : transaction.getAmountForUsers()) {
                amountArray.add(amount);
            }
            node.set("amountForUsers", amountArray);
        }

        if ("equal".equals(transaction.getSplitPaymentType())) {
            node.put("amount", transaction.getSplitAmount());
        }

        if (transaction.getError() != null) {
            node.put("error", transaction.getError());
        }

        output.add(node);
    }

    /**
     *
     * @param transaction
     */
    @Override
    public void visit(final InterestTransaction transaction) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("timestamp", transaction.getTimestamp());
        node.put("currency", transaction.getCurrency());
        node.put("description", transaction.getDescription());
        node.put("amount", transaction.getAmount());
        output.add(node);
    }

    /**
     *
     * @param transaction
     */
    @Override
    public void visit(final InterestRateTransaction transaction) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("timestamp", transaction.getTimestamp());
        node.put("description", transaction.getDescription());
        output.add(node);
    }

    @Override
    public void visit(final WithdrawSavingsTransaction transaction) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        node.put("timestamp", transaction.getTimestamp());

        if (transaction.getError() != null) {
            node.put("description", transaction.getError());
        } else {
            node.put("description", transaction.getDescription());
            if (transaction.getAmount() > 0) {
                node.put("amount", transaction.getAmount());
                node.put("currency", transaction.getCurrency());
            }
        }

        output.add(node);
    }

    @Override
    public void visit(final UpgradePlanTransaction transaction) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        node.put("timestamp", transaction.getTimestamp());
        node.put("description", transaction.getDescription());
        node.put("accountIBAN", transaction.getAccountIBAN());
        node.put("newPlanType", transaction.getNewPlanType());

        output.add(node);
    }

    @Override
    public void visit(final CashWithdrawalTransaction transaction) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        node.put("timestamp", transaction.getTimestamp());
        node.put("description", transaction.getDescription());
        node.put("amount", transaction.getAmount());

        output.add(node);
    }

}
