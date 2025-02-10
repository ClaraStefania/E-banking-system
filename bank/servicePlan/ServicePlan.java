package org.poo.bank.servicePlan;

import org.poo.bank.account.Account;
import org.poo.bank.exchange.CurrencyGraph;

public abstract class ServicePlan {
    protected double commission;
    protected String type;

    public ServicePlan(final double commission, final String type) {
        this.commission = commission;
        this.type = type;
    }

    /**
     * gets the type of the plan
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * sets the type of the plan
     * @param type the type to set
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * calculates the commission
     * @return the new amount
     */
    public abstract double calculateCommission(double amount, CurrencyGraph currencyGraph,
                                               String currency);

    /**
     * pays the fee for the account upgrade
     * @param account the account to pay the fee for
     * @param typeOfPlan the type of the account
     */
    public abstract void payFee(Account account, String typeOfPlan);

    /**
     * calculates the cashback
     * @param amount the amount to calculate the cashback for
     * @param currencyGraph the currency graph
     * @param amountPayed the amount paid
     * @param currency the currency
     * @return the cashback
     */
    public abstract double calculateCashback(double amount, CurrencyGraph currencyGraph,
                                             double amountPayed, String currency);
}
