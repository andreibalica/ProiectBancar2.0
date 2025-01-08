package org.poo.packagePOO.Bank.Account.TransactionsHistory;

public final class CardCreateDeleteTransaction extends TransactionHistory {
    private final String card;
    private final String cardHolder;
    private final String account;

    /**
     *
     * @param timestamp
     * @param description
     * @param card
     * @param cardHolder
     * @param account
     */
    public CardCreateDeleteTransaction(final int timestamp,
                                       final String description,
                                       final String card,
                                       final String cardHolder,
                                       final String account) {
        super(timestamp, description);
        this.card = card;
        this.cardHolder = cardHolder;
        this.account = account;
    }

    /**
     *
     * @return
     */
    public String getCard() {
        return card;
    }

    /**
     *
     * @return
     */
    public String getCardHolder() {
        return cardHolder;
    }

    /**
     *
     * @return
     */
    public String getAccount() {
        return account;
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
