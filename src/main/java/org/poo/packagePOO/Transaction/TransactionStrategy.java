package org.poo.packagePOO.Transaction;

public interface TransactionStrategy {
    /**
     *
     * @return
     */
    boolean validate();

    /**
     *
     * @return
     */
    boolean process();

    /**
     *
     * @return
     */
    String getError();
}
