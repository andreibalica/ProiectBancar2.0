package org.poo.packagePOO;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.packagePOO.Bank.Bank;
import org.poo.utils.Utils;

public final class GlobalManager {
    private static final GlobalManager GLOBAL = new GlobalManager();
    private Bank bank;
    private ArrayNode output;

    private GlobalManager() {
        this.bank = new Bank();
    }

    /**
     *
     * @return
     */
    public static GlobalManager getGlobal() {
        return GLOBAL;
    }

    /**
     *
     * @param bank
     */
    public void setBank(final Bank bank) {
        this.bank = bank;
    }

    /**
     *
     * @return
     */
    public Bank getBank() {
        return bank;
    }

    /**
     *
     * @param output
     */
    public void setOutput(final ArrayNode output) {
        this.output = output;
    }

    /**
     *
     * @return
     */
    public ArrayNode getOutput() {
        return output;
    }

    /**
     *
     */
    public void newBank() {
        Utils.resetRandom();
        this.bank = new Bank();
    }
}
