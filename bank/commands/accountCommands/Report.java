package org.poo.bank.commands.accountCommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.*;
import org.poo.bank.account.Account;
import org.poo.fileio.CommandInput;

import java.util.List;

public class Report implements Command {
    private final CommandInput command;
    private final List<User> users;
    private final ObjectMapper objectMapper;
    private final ArrayNode output;

    public Report(final CommandInput command, final List<User> users, final ArrayNode output) {
        this.command = command;
        this.users = users;
        this.objectMapper = new ObjectMapper();
        this.output = output;
    }

    /**
     * computes the report of the account adding the transactions within the interval
     */
    @Override
    public void execute() {
        int startTimestamp = command.getStartTimestamp();
        int endTimestamp = command.getEndTimestamp();
        String ibanToFind = command.getAccount();

        Account accountAfterIban = CommandsUtils.getAccountAfterIban(users, ibanToFind);

        // if the account is not found, it will output an error message
        if (accountAfterIban == null) {
            ObjectNode rootNode = objectMapper.createObjectNode();
            rootNode.put("command", Constants.REPORT);
            ObjectNode reportNode = objectMapper.createObjectNode();
            reportNode.put("timestamp", command.getTimestamp());
            reportNode.put("description", Constants.ACCOUNT_NOT_FOUND);
            rootNode.set("output", reportNode);
            rootNode.put("timestamp", command.getTimestamp());
            output.add(rootNode);
            return;
        }

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("command", Constants.REPORT);
        ObjectNode reportNode = objectMapper.createObjectNode();
        reportNode.put("IBAN", ibanToFind);
        reportNode.put("balance", accountAfterIban.getBalance());
        reportNode.put("currency", accountAfterIban.getCurrency());


        ArrayNode transactionsArray = objectMapper.createArrayNode();

        // adds the transactions within the interval to the output
        for (Transaction transaction : accountAfterIban.getTransactions()) {
            if (transaction.getTimestamp() < startTimestamp
                    || transaction.getTimestamp() > endTimestamp) {
                continue;
            }
            ObjectNode transactionNode = transaction.computeTransactionOutput(objectMapper,
                                                                                transaction);
            transactionsArray.add(transactionNode);
        }

        reportNode.set("transactions", transactionsArray);
        rootNode.set("output", reportNode);
        rootNode.put("timestamp", command.getTimestamp());
        output.add(rootNode);
    }
}
