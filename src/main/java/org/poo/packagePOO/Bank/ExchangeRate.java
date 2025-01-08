package org.poo.packagePOO.Bank;

public final class ExchangeRate {
    private final String from;
    private final String to;
    private final double rate;

    /**
     *
     * @param from
     * @param to
     * @param rate
     */
    public ExchangeRate(final String from,
                        final String to,
                        final double rate) {
        this.from = from;
        this.to = to;
        this.rate = rate;
    }

    /**
     *
     * @return
     */
    public String getFrom() {
        return from;
    }

    /**
     *
     * @return
     */
    public String getTo() {
        return to;
    }

    /**
     *
     * @return
     */
    public double getRate() {
        return rate;
    }
}
