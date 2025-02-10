package org.poo.bank.commands.accountCommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Command;
import org.poo.bank.User;
import org.poo.bank.Constants;
import org.poo.bank.CommandsUtils;
import org.poo.bank.account.Account;
import org.poo.bank.account.BusinessAccount;
import org.poo.fileio.CommandInput;

import java.util.List;

public class ChangeSpendingLimit implements Command {
    private final CommandInput command;
    private final List<User> users;
    private final ArrayNode output;

    public ChangeSpendingLimit(final CommandInput command, final List<User> users,
                               final ArrayNode output) {
        this.command = command;
        this.users = users;
        this.output = output;
    }

    /**
     * changes the spending limit for the business account
     */
    @Override
    public void execute() {
        ObjectMapper objectMapper = new ObjectMapper();
        String iban = command.getAccount();
        Account account = CommandsUtils.getAccountAfterIban(users, iban);
        String email = command.getEmail();

        if (account == null) {
            return;
        }

        if (!account.getType().equals(Constants.BUSINESS_ACCOUNT)) {
            ObjectNode rootNode = objectMapper.createObjectNode();
            rootNode.put("command", Constants.CHANGE_SPENDING_LIMIT);
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("timestamp", command.getTimestamp());
            errorNode.put("description", "This is not a business account");
            rootNode.set("output", errorNode);
            rootNode.put("timestamp", command.getTimestamp());
            output.add(rootNode);
            return;
        }

        User user = CommandsUtils.getUserAfterEmail(users, email);
        if (user == null) {
            return;
        }

        // check if the user is the owner of the account
        String accountOwnerEmail = ((BusinessAccount) account).getOwner().getEmail();
        if (user.getEmail().equals(accountOwnerEmail)) {
            ((BusinessAccount) account).setPaymentLimit(command.getAmount());
        } else {
            ObjectNode rootNode = objectMapper.createObjectNode();
            rootNode.put("command", Constants.CHANGE_SPENDING_LIMIT);
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("timestamp", command.getTimestamp());
            errorNode.put("description", "You must be owner in order to change spending limit.");
            rootNode.set("output", errorNode);
            rootNode.put("timestamp", command.getTimestamp());
            output.add(rootNode);
        }
    }
}
