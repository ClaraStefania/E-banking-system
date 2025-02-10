package org.poo.bank;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.account.Account;
import org.poo.bank.account.AccountFactory;
import org.poo.bank.exchange.CurrencyGraph;
import org.poo.fileio.CommerciantInput;
import org.poo.fileio.ExchangeInput;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ObjectInput;
import org.poo.fileio.UserInput;
import java.util.LinkedList;
import java.util.List;

public final class BankManagement {

    private static BankManagement singleInstance = null;

    private BankManagement() {
    }

    /**
     * gets the instance of the bank management
     * @return the instance of the bank management
     */
    public static BankManagement getInstance() {
        if (singleInstance == null) {
            singleInstance = new BankManagement();
        }
        return singleInstance;
    }

    /**
     * puts the users in a list, creates the currency graph and manages the commands
     * @param inputData the input data
     * @param output the output array
     */
    public void manageBank(final ObjectInput inputData, final ArrayNode output) {
        UserInput[] usersInput = inputData.getUsers();
        CommerciantInput[] commerciantsInput = inputData.getCommerciants();
        List<User> users = new LinkedList<>();
        List<Commerciant> commerciants = new LinkedList<>();
        CommandInput[] commands = inputData.getCommands();
        CommandsManagement commandsManagement = CommandsManagement.getInstance();
        ExchangeInput[] exchanges = inputData.getExchangeRates();

        for (UserInput user : usersInput) {
            User newUser = new User(user.getFirstName(), user.getLastName(), user.getEmail(),
                    user.getBirthDate(), user.getOccupation());
            users.add(newUser);
        }

        AccountFactory accountFactory = new AccountFactory();
        CurrencyGraph currencyGraph = new CurrencyGraph();
        for (CommerciantInput commerciant : commerciantsInput) {
            Account newAccount = accountFactory.createAccount(commerciant.getAccount(),
                    Constants.RON, Constants.CLASSIC_ACCOUNT,
                    currencyGraph, 0, 0, null, null);
            Commerciant newCommerciant = new Commerciant(commerciant.getCommerciant(), 0,
                    commerciant.getId(), newAccount, commerciant.getType(),
                    commerciant.getCashbackStrategy());
            commerciants.add(newCommerciant);
        }

        currencyGraph.buildExchangeGraph(exchanges);
        List<SplitPaymentClass> splitPayments = new LinkedList<>();
        commandsManagement.manageCommands(commands, users, output, currencyGraph,
                commerciants, splitPayments);
    }
}
