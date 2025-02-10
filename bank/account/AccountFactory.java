package org.poo.bank.account;

import org.poo.bank.Constants;
import org.poo.bank.User;
import org.poo.bank.cashbackPlan.NrOfTransactions;
import org.poo.bank.exchange.CurrencyGraph;
import java.util.Map;

public class AccountFactory {
    /**
     * Creates an account based on the given parameters.
     * @param iban the IBAN of the account
     * @param currency the currency of the account
     * @param type the type of the account
     * @param currencyGraph the currency graph
     * @param interestRate the interest rate of the account
     * @return the created account
     */
    public Account createAccount(final String iban, final String currency, final String type,
                                 final CurrencyGraph currencyGraph, final double interestRate,
                                 final int usersAge, final User owner,
                                 final Map<String, NrOfTransactions> nrOfTransactionsMap) {
        return switch (type) {
            case Constants.SAVINGS_ACCOUNT ->
                    new SavingsAccount(iban, currency, Constants.SAVINGS_ACCOUNT,
                            currencyGraph, interestRate, usersAge, nrOfTransactionsMap);
            case Constants.CLASSIC_ACCOUNT ->
                    new ClassicAccount(iban, currency, Constants.CLASSIC_ACCOUNT, currencyGraph,
                            usersAge, nrOfTransactionsMap);
            case Constants.BUSINESS_ACCOUNT ->
                    new BusinessAccount(iban, currency, Constants.BUSINESS_ACCOUNT, currencyGraph,
                            usersAge, owner, nrOfTransactionsMap);
            default -> throw new IllegalArgumentException("The account type " + type
                    + " is not recognized.");
        };
    }
}
