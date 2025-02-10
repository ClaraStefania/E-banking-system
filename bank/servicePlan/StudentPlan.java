package org.poo.bank.servicePlan;

import org.poo.bank.Constants;
import org.poo.bank.account.Account;
import org.poo.bank.exchange.CurrencyGraph;

public class StudentPlan extends ServicePlan {
    public StudentPlan() {
        super(0, Constants.STUDENT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double calculateCommission(final double amount, final CurrencyGraph currencyGraph,
                                      final String currency) {
        return amount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void payFee(final Account account, final String type) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double calculateCashback(final double amount, final CurrencyGraph currencyGraph,
                                    final double amountPayed, final String currency) {
        double exchangeRate = currencyGraph.calculateExchangeRate(currency, Constants.RON);
        double amountInRon = amountPayed * exchangeRate;

        if (amountInRon >= Constants.LIMIT_100 && amountInRon < Constants.LIMIT_300) {
            return amount * Constants.CASHBACK_STANDARD_LOW_AMOUNT;
        } else if (amountInRon >= Constants.LIMIT_300 && amountInRon < Constants.LIMIT_500) {
            return amount * Constants.CASHBACK_STANDARD_MEDIUM_AMOUNT;
        } else if (amountInRon >= Constants.LIMIT_500) {
            return amount * Constants.CASHBACK_STANDARD_BIG_AMOUNT;
        } else {
            return 0;
        }
    }
}
