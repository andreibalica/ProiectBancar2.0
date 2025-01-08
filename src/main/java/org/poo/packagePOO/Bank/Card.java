package org.poo.packagePOO.Bank;

public final class Card {
    private final String email;
    private final String cardNumber;
    private final String iban;
    private final int timestamp;
    private String status;
    private int use;

    /**
     *
     * @param email
     * @param cardNumber
     * @param iban
     * @param timestamp
     */
    public Card(final String email,
                final String cardNumber,
                final String iban,
                final int timestamp) {
        this.email = email;
        this.cardNumber = cardNumber;
        this.iban = iban;
        this.timestamp = timestamp;
        this.status = "active";
        this.use = 1;
    }

    /**
     *
     * @param use
     */
    public void setUse(final int use) {
        this.use = use;
    }

    /**
     *
     * @return
     */
    public int getUse() {
        return use;
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
    public String getEmail() {
        return email;
    }

    /**
     *
     * @return
     */
    public String getIBAN() {
        return iban;
    }

    /**
     *
     * @return
     */
    public int getTimestamp() {
        return timestamp;
    }

    /**
     *
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     */
    public void setStatus(final String status) {
        this.status = status;
    }
}
