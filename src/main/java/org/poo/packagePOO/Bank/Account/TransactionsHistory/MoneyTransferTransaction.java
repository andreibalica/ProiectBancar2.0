package org.poo.packagePOO.Bank.Account.TransactionsHistory;

public final class MoneyTransferTransaction extends TransactionHistory {
    private final String senderIBAN;
    private final String receiverIBAN;
    private final String amount;
    private final String transferType;

    /**
     *
     * @param timestamp
     * @param description
     * @param senderIBAN
     * @param receiverIBAN
     * @param amount
     * @param currency
     * @param transferType
     */
    public MoneyTransferTransaction(final int timestamp,
                                    final String description,
                                    final String senderIBAN,
                                    final String receiverIBAN,
                                    final double amount,
                                    final String currency,
                                    final String transferType) {
        super(timestamp, description);
        this.senderIBAN = senderIBAN;
        this.receiverIBAN = receiverIBAN;
        this.amount = amount + " " + currency;
        this.transferType = transferType;
    }

    /**
     *
     * @return
     */
    public String getSenderIBAN() {
        return senderIBAN;
    }

    /**
     *
     * @return
     */
    public String getReceiverIBAN() {
        return receiverIBAN;
    }

    /**
     *
     * @return
     */
    public String getAmount() {
        return amount;
    }

    /**
     *
     * @return
     */
    public String getTransferType() {
        return transferType;
    }

    /**
     *
     * @param visitor
     */
    @Override
    public void accept(final TransactionVisitor visitor) {
        visitor.visit(this);
    }
}
