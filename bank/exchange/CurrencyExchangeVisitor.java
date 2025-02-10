package org.poo.bank.exchange;

public interface CurrencyExchangeVisitor {
    /**
     * visits a node
     * @param node the node to visit
     */
    void visit(CurrencyNode node);
}
