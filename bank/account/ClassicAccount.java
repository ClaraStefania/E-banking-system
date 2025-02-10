package org.poo.bank.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Constants;
import org.poo.bank.cashbackPlan.NrOfTransactions;
import org.poo.bank.exchange.CurrencyGraph;
import org.poo.fileio.CommandInput;

import java.util.Map;

public class ClassicAccount extends Account {
    public ClassicAccount(final String iban, final String currency, final String type,
                          final CurrencyGraph currencyGraph, final int usersAge,
                          final Map<String, NrOfTransactions> nrOfTransactionsMap) {
        super(iban, currency, type, currencyGraph, usersAge, nrOfTransactionsMap);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectNode computeErrorOutput(final ObjectMapper objectMapper,
                                         final CommandInput command) {
        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("command", command.getCommand());
        ObjectNode reportNode = objectMapper.createObjectNode();
        reportNode.put("timestamp", command.getTimestamp());
        reportNode.put("description", Constants.THIS_IS_NOT_A_SAVINGS_ACCOUNT);
        rootNode.set("output", reportNode);
        rootNode.put("timestamp", command.getTimestamp());
        return rootNode;
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
}
