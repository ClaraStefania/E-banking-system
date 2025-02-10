package org.poo.bank.commands.accountCommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.poo.bank.*;
import org.poo.bank.account.Account;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class SpendingsReport implements Command {
    private final CommandInput command;
    private final List<User> users;
    private final ObjectMapper objectMapper;
    private final ArrayNode output;
    private final List<Transaction> validTransaction;
    private final List<Commerciant> commerciants;

    public SpendingsReport(final CommandInput command, final List<User> users,
                           final ArrayNode output) {
        this.command = command;
        this.users = users;
        this.objectMapper = new ObjectMapper();
        this.output = output;
        this.validTransaction = new ArrayList<>();
        this.commerciants = new ArrayList<>();
    }

    /**
     * sorts the commerciants by name
     * @param commerciantsToSort the list of commerciants to be sorted
     */
    private void sortCommerciants(final List<Commerciant> commerciantsToSort) {
        commerciantsToSort.sort(Comparator.comparing(Commerciant::getCommerciantName));
    }

    /**
     * creates the spendings report, adding the transactions and the commerciants
     * for the error cases, it will compute the error output
     */
    @Override
    public void execute() {
        int startTimestamp = command.getStartTimestamp();
        int endTimestamp = command.getEndTimestamp();
        String ibanToFind = command.getAccount();

        Account accountAfterIban = CommandsUtils.getAccountAfterIban(users, ibanToFind);
        if (accountAfterIban == null) {
            ObjectNode rootNode = objectMapper.createObjectNode();
            rootNode.put("command", Constants.SPENDINGS_REPORT);
            ObjectNode reportNode = objectMapper.createObjectNode();
            reportNode.put("timestamp", command.getTimestamp());
            reportNode.put("description", Constants.ACCOUNT_NOT_FOUND);
            rootNode.set("output", reportNode);
            rootNode.put("timestamp", command.getTimestamp());
            output.add(rootNode);
            return;
        }

        // if the account is a saving account, it will output an error message
        if (accountAfterIban.getType().equals(Constants.SAVINGS_ACCOUNT)) {
            ObjectNode rootNode = objectMapper.createObjectNode();
            rootNode.put("command", Constants.SPENDINGS_REPORT);
            ObjectNode reportNode = objectMapper.createObjectNode();
            reportNode.put("error", "This kind of report is not supported for a saving account");
            rootNode.set("output", reportNode);
            rootNode.put("timestamp", command.getTimestamp());
            output.add(rootNode);
            return;
        }

        // for each commerciant is calculated the total amount spent
        List<Transaction> transactions = accountAfterIban.getTransactions();
        for (Transaction transaction : transactions) {
            if (transaction.getTimestamp() >= startTimestamp
                    && transaction.getTimestamp() <= endTimestamp
                    && transaction.getCommerciant() != null) {
                validTransaction.add(transaction);
                Commerciant commerciant = CommandsUtils.verifyIfCommerciantExists(commerciants,
                                                                    transaction.getCommerciant());
                if (commerciant == null) {
                    commerciant = new Commerciant(transaction.getCommerciant(),
                            transaction.getCardPayment(), 0, null, "", "");
                    commerciants.add(commerciant);
                } else {
                    commerciant.setAmount(commerciant.getAmount() + transaction.getCardPayment());
                }
            }
        }

        sortCommerciants(commerciants);

        // creates the spendings report
        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("command", Constants.SPENDINGS_REPORT);
        ObjectNode reportNode = objectMapper.createObjectNode();
        reportNode.put("IBAN", ibanToFind);
        reportNode.put("balance", accountAfterIban.getBalance());
        reportNode.put("currency", accountAfterIban.getCurrency());

        // adds the transactions and the commerciants to the output
        ArrayNode transactionsArray = objectMapper.createArrayNode();
        for (Transaction transaction : validTransaction) {
            ObjectNode transactionNode = transaction.computeTransactionOutput(objectMapper,
                                                                                transaction);
            transactionsArray.add(transactionNode);
        }

        ArrayNode commetrciantsArray = objectMapper.createArrayNode();
        for (Commerciant commerciant : commerciants) {
            ObjectNode commerciantNode = objectMapper.createObjectNode();
            commerciantNode.put("commerciant", commerciant.getCommerciantName());
            commerciantNode.put("total", commerciant.getAmount());
            commetrciantsArray.add(commerciantNode);
        }

        reportNode.set("transactions", transactionsArray);
        reportNode.set("commerciants", commetrciantsArray);
        rootNode.set("output", reportNode);
        rootNode.put("timestamp", command.getTimestamp());
        output.add(rootNode);
    }
}
