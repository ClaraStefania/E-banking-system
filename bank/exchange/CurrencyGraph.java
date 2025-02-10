package org.poo.bank.exchange;

import org.poo.fileio.ExchangeInput;

import java.util.HashMap;
import java.util.Map;

public class CurrencyGraph {
    private Map<String, CurrencyNode> currencyGraph = new HashMap<>();

    /**
     * gets the currency graph
     * @return the value of currencyGraph
     */
    public Map<String, CurrencyNode> getCurrencyGraph() {
        return currencyGraph;
    }

    /**
     * sets the currency graph
     * @param currencyGraph the value to set
     */
    public void setCurrencyGraph(final Map<String, CurrencyNode> currencyGraph) {
        this.currencyGraph = currencyGraph;
    }

    /**
     * builds the exchange graph
     * @param exchanges the exchanges
     */
    public void buildExchangeGraph(final ExchangeInput[] exchanges) {
        for (ExchangeInput exchange : exchanges) {
            double rate = exchange.getRate();
            double inverseRate = 1.0 / rate;

            String fromCurrency = exchange.getFrom();
            String toCurrency = exchange.getTo();

            CurrencyNode fromNode = currencyGraph.computeIfAbsent(fromCurrency, CurrencyNode::new);
            CurrencyNode toNode = currencyGraph.computeIfAbsent(toCurrency, CurrencyNode::new);

            fromNode.addEdge(toNode, rate);
            toNode.addEdge(fromNode, inverseRate);
        }
    }

    /**
     * calculates the exchange rate
     * @param fromCurrency the currency to convert from
     * @param toCurrency the currency to convert to
     * @return the exchange rate
     */
    public double calculateExchangeRate(final String fromCurrency, final String toCurrency) {
        if (fromCurrency == null || toCurrency == null) {
            return 1.0;
        }

        if (fromCurrency.equals(toCurrency)) {
            return 1.0;
        }

        CurrencyNode fromNode = currencyGraph.get(fromCurrency);
        if (fromNode == null) {
            return 0.0;
        }

        ExchangeRateCalculatorVisitor visitor = new ExchangeRateCalculatorVisitor(toCurrency);
        fromNode.accept(visitor);

        return visitor.getRate();
    }
}
