package org.poo.bank.commands.cardCommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Constants;
import org.poo.bank.User;
import org.poo.bank.CommandsUtils;
import org.poo.bank.Command;
import org.poo.bank.Transaction;
import org.poo.bank.account.Account;
import org.poo.bank.card.Card;
import org.poo.fileio.CommandInput;

import java.util.List;

public class CheckCardStatus implements Command {
    private final CommandInput command;
    private final List<User> users;
    private final ArrayNode output;

    public CheckCardStatus(final CommandInput command, final List<User> users,
                           final ArrayNode output) {
        this.command = command;
        this.users = users;
        this.output = output;
    }

    /**
     * checks if the card has to be frozen
     * in case the card is not found, it will compute the error output
     * if the balance is less than the minimum balance, the card will be frozen and a transaction
     * will be added
     */
    @Override
    public void execute() {

        for (User user : users) {
            for (Account account : user.getAccounts()) {
                Card card = CommandsUtils.getCardAfterNumber(account, command.getCardNumber());
                if (card != null && account.getBalance() <= account.getMinBalance()) {
                    card.setStatus(Constants.FROZEN);
                    Transaction transaction = new Transaction.Builder()
                            .setTimestamp(command.getTimestamp())
                            .setDescription(Constants.REACHED_MINIMUM_AMOUNT)
                            .build();
                    account.getTransactions().add(transaction);
                    return;
                } else if (card != null) {
                    return;
                }
            }
        }

        // computes the error output
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("command", Constants.CHECK_CARD_STATUS);
        ObjectNode node = objectMapper.createObjectNode();
        node.put("timestamp", command.getTimestamp());
        node.put("description", Constants.CARD_NOT_FOUND);
        rootNode.set("output", node);
        rootNode.put("timestamp", command.getTimestamp());
        output.add(rootNode);
    }
}
