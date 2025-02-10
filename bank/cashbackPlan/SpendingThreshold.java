package org.poo.bank.cashbackPlan;

import org.poo.bank.exchange.CurrencyGraph;
import org.poo.bank.servicePlan.ServicePlan;

public class SpendingThreshold {
    private double amountPaid;

    public SpendingThreshold(final double amountPaid) {
        this.amountPaid = amountPaid;
    }

    /**
     * gets the amount paid
     * @return the value of amountPayed
     */
    public double getAmountPaid() {
        return amountPaid;
    }

    /**
     * sets the amount paid
     * @param amountPayed the new amount paid
     */
    public void setAmountPaid(final double amountPayed) {
        this.amountPaid = amountPayed;
    }

    /**
     * calculates the cashback
     * @param amount the amount to calculate the cashback
     * @param servicePlan the service plan to calculate the cashback
     * @param amountPayed the amount paid
     * @param currencyGraph the currency graph
     * @param currency the currency
     * @return the cashback
     */
    public double calculateCashback(final double amount, final ServicePlan servicePlan,
                                    final double amountPayed, final CurrencyGraph currencyGraph,
                                    final String currency) {
        return servicePlan.calculateCashback(amount, currencyGraph, amountPayed, currency);
    }
}
