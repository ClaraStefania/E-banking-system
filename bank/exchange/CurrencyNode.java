package org.poo.bank.exchange;

import java.util.HashMap;
import java.util.Map;

public class CurrencyNode {
    private final String currencyNode;
    private final Map<CurrencyNode, Double> neighbors;

    public CurrencyNode(final String currencyNode) {
        this.currencyNode = currencyNode;
        this.neighbors = new HashMap<>();
    }

    /**
     * gets the neighbors
     * @return the value of neighbors
     */
    public Map<CurrencyNode, Double> getNeighbors() {
        return neighbors;
    }

    /**
        * gets the currency code
     *
     */
    public String getCurrencyNode() {
        return currencyNode;
    }

    /**
     * adds an edge
     * @param target the target
     * @param rate the rate
     */
    public void addEdge(final CurrencyNode target, final double rate) {
        neighbors.put(target, rate);
    }

    /**
     * accepts a visitor
     * @param visitor the visitor
     */
    public void accept(final CurrencyExchangeVisitor visitor) {
        visitor.visit(this);
    }
}
