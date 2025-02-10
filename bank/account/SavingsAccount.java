package org.poo.bank.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Constants;
import org.poo.bank.Transaction;
import org.poo.bank.cashbackPlan.NrOfTransactions;
import org.poo.bank.exchange.CurrencyGraph;
import org.poo.fileio.CommandInput;

import java.util.Map;

public class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(final String iban, final String currency, final String type,
                          final CurrencyGraph currencyGraph, final double interestRate,
                          final int usersAge,
                          final Map<String, NrOfTransactions> nrOfTransactionsMap) {
        super(iban, currency, type, currencyGraph, usersAge, nrOfTransactionsMap);
        this.interestRate = interestRate;
    }

    /**
     * gets the account's interest rate
     * @return the value of interestRate
     */
    public double getInterestRate() {
        return interestRate;
    }

    /**
     * sets the interest rate of the account
     * @param interestRate the new interest rate
     */
    public void setInterestRate(final double interestRate) {
        this.interestRate = interestRate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectNode computeErrorOutput(final ObjectMapper objectMapper,
                                         final CommandInput command) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addFunds(final double amount, final String userEmail) {
        balance += amount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double acceptPayment(final double amount, final String paymentCurrency) {
        double exchangeRate = currencyGraph.calculateExchangeRate(paymentCurrency, currency);
        balance += amount * exchangeRate;
        return amount * exchangeRate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int payOnline(final double amount, final String paymentCurrency) {
        if (amount == 0) {
            return 1;
        }

        double exchangeRate = currencyGraph.calculateExchangeRate(paymentCurrency, currency);
        double newBalance = balance - amount * exchangeRate;

        // verify if the payment can be made
        if (newBalance >= 0) {
            balance = newBalance;
            return 0;
        }

        return 1;
    }

    /**
     * withdraws money from the account
     * @param amount the amount to be withdrawn
     * @param timestamp the timestamp of the transaction
     */
    public void withdrawSavings(final double amount, final int timestamp) {
        if (usersAge < Constants.MINIMUM_AGE) {
            Transaction transaction = new Transaction.Builder()
                .setDescription("You don't have the minimum age required.")
                .setTimestamp(timestamp)
                .build();
            transactions.add(transaction);
            return;
        }

        // verify if the new balance is negative
        if (balance - amount < 0) {
            return;
        }

        balance -= amount;
    }
}
