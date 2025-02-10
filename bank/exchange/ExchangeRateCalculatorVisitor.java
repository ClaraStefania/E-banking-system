package org.poo.bank.exchange;

import java.util.HashMap;
import java.util.Map;

public class ExchangeRateCalculatorVisitor implements CurrencyExchangeVisitor {
    private final String targetCurrency;
    private double accumulatedRate = 1.0;
    private boolean found = false;
    private final Map<CurrencyNode, Double> visited = new HashMap<>();

    public ExchangeRateCalculatorVisitor(final String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    /**
     * gets the rate
     * @return the rate
     */
    public double getRate() {
        if (found) {
            return accumulatedRate;
        } else {
            return 0.0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit(final CurrencyNode node) {
        if (visited.containsKey(node)) {
            return;
        }

        visited.put(node, accumulatedRate);

        if (node.getCurrencyNode().equals(targetCurrency)) {
            found = true;
            return;
        }

        for (Map.Entry<CurrencyNode, Double> neighbor : node.getNeighbors().entrySet()) {
            if (!found) {
                double previousRate = accumulatedRate;
                accumulatedRate *= neighbor.getValue();
                neighbor.getKey().accept(this);
                if (found) {
                    return;
                }
                accumulatedRate = previousRate;
            }
        }
    }
}
