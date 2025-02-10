package org.poo.bank.commands.accountCommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Constants;
import org.poo.bank.CommandsUtils;
import org.poo.bank.User;
import org.poo.bank.Command;
import org.poo.bank.Transaction;
import org.poo.bank.account.Account;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PrintTransactions implements Command {
    private final CommandInput command;
    private final List<User> users;
    private final ArrayNode output;
    private final ObjectMapper objectMapper;

    public PrintTransactions(final CommandInput command, final List<User> users,
                             final ArrayNode output) {
        this.command = command;
        this.users = users;
        this.output = output;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * sorts the transactions by timestamp
     * @param transactions the list of transactions to be sorted
     */
    public void sortTransactions(final List<Transaction> transactions) {
        transactions.sort(Comparator.comparingInt(Transaction::getTimestamp));
    }

    /**
     * puts the transactions of the user in the output
     */
    @Override
    public void execute() {
        String email = command.getEmail();
        User user = CommandsUtils.getUserAfterEmail(users, email);
        if (user == null) {
            return;
        }

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("command", Constants.PRINT_TRANSACTIONS);

        ArrayNode transactionsArray = objectMapper.createArrayNode();
        List<Transaction> transactions = new ArrayList<>();

        // adds to the list all the transactions from all the accounts of the user
        for (Account account : user.getAccounts()) {
            transactions.addAll(account.getTransactions());
        }

        sortTransactions(transactions);

        for (Transaction transaction : transactions) {
            ObjectNode transactionNode = transaction.computeTransactionOutput(objectMapper,
                                                                                transaction);
            transactionsArray.add(transactionNode);
        }

        rootNode.set("output", transactionsArray);
        rootNode.put("timestamp", command.getTimestamp());
        output.add(rootNode);
    }
}
