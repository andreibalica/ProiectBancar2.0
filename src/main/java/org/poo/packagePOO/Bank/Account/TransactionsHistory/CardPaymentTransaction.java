package org.poo.packagePOO.Bank.Account.TransactionsHistory;

public final class CardPaymentTransaction extends TransactionHistory {
    private final String cardNumber;
    private final double amount;
    private final String currency;
    private final String commerciant;

    /**
     *
     * @param timestamp
     * @param description
     * @param cardNumber
     * @param amount
     * @param currency
     * @param commerciant
     */
    public CardPaymentTransaction(final int timestamp,
                                  final String description,
                                  final String cardNumber,
                                  final double amount,
                                  final String currency,
                                  final String commerciant) {
        super(timestamp, description);
        this.cardNumber = cardNumber;
        this.amount = amount;
        this.currency = currency;
        this.commerciant = commerciant;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isPayment() {
        return true;
    }

    /**
     *
     * @param visitor
     */
    @Override
    public void accept(final TransactionVisitor visitor) {
        visitor.visit(this);
    }

    /**
     *
     * @return
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     *
     * @return
     */
    public double getAmount() {
        return amount;
    }

    /**
     *
     * @return
     */
    public String getCurrency() {
        return currency;
    }

    /**
     *
     * @return
     */
    public String getCommerciant() {
        return commerciant;
    }
}
