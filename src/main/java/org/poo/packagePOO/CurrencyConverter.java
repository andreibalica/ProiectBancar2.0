package org.poo.packagePOO;

import org.poo.packagePOO.Bank.Bank;
import org.poo.packagePOO.Bank.ExchangeRate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public final class CurrencyConverter {
    private static final CurrencyConverter CONVERTER = new CurrencyConverter();
    private Map<String, Map<String, Double>> exchangeRates;

    /**
     *
     */
    private CurrencyConverter() {
        this.exchangeRates = new HashMap<>();
    }

    /**
     *
     * @return
     */
    public static CurrencyConverter getConverter() {
        return CONVERTER;
    }

    /**
     *
     */
    public void newConverter() {
        this.exchangeRates = new HashMap<>();
    }

    /**
     *
     */
    public void initializeaza() {
        exchangeRates.clear();
        Bank bank = GlobalManager.getGlobal().getBank();

        for (ExchangeRate rate : bank.getExchangeRates()) {
            addRate(rate.getFrom(), rate.getTo(), rate.getRate());
            addRate(rate.getTo(), rate.getFrom(), 1.0 / rate.getRate());
        }
    }

    /**
     *
     * @param from
     * @param to
     * @param rate
     */
    private void addRate(final String from, final String to, final double rate) {
        exchangeRates.computeIfAbsent(from, k -> new HashMap<>()).put(to, rate);
    }

    /**
     *
     * @param from
     * @param to
     * @param amount
     * @return
     */
    public double convert(final String from, final String to,
                          final double amount) {
        if (from.equals(to)) {
            return amount;
        }

        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        Map<String, Double> rates = new HashMap<>();
        Map<String, String> path = new HashMap<>();

        queue.add(from);
        rates.put(from, 1.0);
        visited.add(from);

        while (!queue.isEmpty()) {
            String current = queue.poll();

            if (!exchangeRates.containsKey(current)) {
                continue;
            }

            for (Map.Entry<String, Double> neighbor
                    : exchangeRates.get(current).entrySet()) {
                String nextCurrency = neighbor.getKey();
                if (!visited.contains(nextCurrency)) {
                    visited.add(nextCurrency);
                    queue.add(nextCurrency);
                    rates.put(nextCurrency,
                            rates.get(current) * neighbor.getValue());
                    path.put(nextCurrency, current);

                    if (nextCurrency.equals(to)) {
                        return amount * rates.get(to);
                    }
                }
            }
        }

        throw new IllegalArgumentException(
                String.format("No conversion path found from %s to %s", from, to)
        );
    }
}
