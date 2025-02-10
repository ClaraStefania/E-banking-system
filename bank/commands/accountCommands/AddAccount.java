package org.poo.bank.commands.accountCommands;

import org.poo.bank.Command;
import org.poo.bank.Commerciant;
import org.poo.bank.Constants;
import org.poo.bank.Transaction;
import org.poo.bank.User;
import org.poo.bank.CommandsUtils;
import org.poo.bank.account.Account;
import org.poo.bank.account.AccountFactory;
import org.poo.bank.cashbackPlan.NrOfTransactions;
import org.poo.bank.exchange.CurrencyGraph;
import org.poo.fileio.CommandInput;
import org.poo.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddAccount implements Command {
    private final CommandInput command;
    private final List<User> users;
    private final CurrencyGraph currencyGraph;
    private final List<Commerciant> commerciants;

    public AddAccount(final CommandInput command, final List<User> users,
                      final CurrencyGraph currencyGraph, final List<Commerciant> commerciants) {
        this.command = command;
        this.users = users;
        this.currencyGraph = currencyGraph;
        this.commerciants = commerciants;
    }

    /**
     * adds a new account to the user
     */
    @Override
    public void execute() {
        String email = command.getEmail();
        String accountType = command.getAccountType();
        String currency = command.getCurrency();
        double interestRate = command.getInterestRate();

        User user = CommandsUtils.getUserAfterEmail(users, email);
        if (user == null) {
            return;
        }

        Map<String, NrOfTransactions> nrOfTransactionsMap = new HashMap<>();
        String iban = Utils.generateIBAN();
        AccountFactory accountFactory = new AccountFactory();

        // creates a list that helps to keep track of the number of transactions
        for (Commerciant commerciant : commerciants) {
            NrOfTransactions nrOfTransactions;
            if (commerciant.getType().equals("Food")) {
                nrOfTransactions = new NrOfTransactions(0, 0, Constants.NR_STANDARD_FOOD, "Food");
            } else if (commerciant.getType().equals("Clothes")) {
                nrOfTransactions = new NrOfTransactions(0, 0,
                        Constants.NR_STANDARD_CLOTHES, "Clothes");
            } else {
               nrOfTransactions = new NrOfTransactions(0, 0, Constants.NR_STANDARD_TECH, "Tech");
            }
            nrOfTransactionsMap.put(commerciant.getCommerciantName(), nrOfTransactions);
        }

        Account newAccount = accountFactory.createAccount(iban, currency, accountType,
                currencyGraph, interestRate, user.getAge(), user, nrOfTransactionsMap);
        newAccount.setMinBalance(0);
        user.getAccounts().add(newAccount);

        if (newAccount.getType().equals(Constants.BUSINESS_ACCOUNT)) {
            return;
        }

        // the transaction is added just for the accounts that are not business accounts
        Transaction transaction = new Transaction.Builder()
                .setDescription("New account created")
                .setTimestamp(command.getTimestamp())
                .build();

        newAccount.getTransactions().add(transaction);
    }
}
