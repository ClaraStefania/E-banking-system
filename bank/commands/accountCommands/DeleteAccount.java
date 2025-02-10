package org.poo.bank.commands.accountCommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.poo.bank.Command;
import org.poo.bank.Constants;
import org.poo.bank.CommandsUtils;
import org.poo.bank.Transaction;
import org.poo.bank.User;
import org.poo.bank.account.Account;
import org.poo.bank.account.BusinessAccount;
import org.poo.fileio.CommandInput;

import java.util.List;

public class DeleteAccount implements Command {
    private final CommandInput command;
    private final List<User> users;
    private final ArrayNode output;

    public DeleteAccount(final CommandInput command, final List<User> users,
                         final ArrayNode output) {
        this.command = command;
        this.users = users;
        this.output = output;
    }

    /**
     * deletes the account if the balance is 0 and computes the output
     */
    @Override
    public void execute() {
        String iban = command.getAccount();
        String email = command.getEmail();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("command", Constants.DELETE_ACCOUNT);
        ObjectNode node = objectMapper.createObjectNode();

        // verify if the user and account exist
        User user = CommandsUtils.getUserAfterEmail(users, email);
        if (user == null) {
            return;
        }

        Account account = CommandsUtils.verifyIfAccountExists(user.getAccounts(), iban);
        if (account == null) {
            return;
        }

        if (account.getType().equals(Constants.BUSINESS_ACCOUNT) &&
                !email.equals(((BusinessAccount) account).getOwner().getEmail())) {
            return;
        }

        // verify if the account has funds
        if (account.getBalance() != 0) {
            node.put("error", Constants.ACCOUNT_NOT_DELETED);
            node.put("timestamp", command.getTimestamp());
            rootNode.set("output", node);
            rootNode.put("timestamp", command.getTimestamp());
            output.add(rootNode);

            // add transaction to account
            Transaction transaction = new Transaction.Builder()
                    .setDescription(Constants.ACCOUNT_HAS_FUNDS)
                    .setTimestamp(command.getTimestamp())
                    .build();
            account.getTransactions().add(transaction);
            return;
        }

        user.getAccounts().remove(account);
        node.put("success", Constants.ACCOUNT_DELETED);
        node.put("timestamp", command.getTimestamp());
        rootNode.set("output", node);
        rootNode.put("timestamp", command.getTimestamp());
        output.add(rootNode);
    }
}
