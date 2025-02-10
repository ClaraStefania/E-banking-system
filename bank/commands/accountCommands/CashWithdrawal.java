package org.poo.bank.commands.accountCommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Command;
import org.poo.bank.User;
import org.poo.bank.Transaction;
import org.poo.bank.Constants;
import org.poo.bank.CommandsUtils;
import org.poo.bank.account.Account;
import org.poo.bank.account.BusinessAccount;
import org.poo.bank.card.Card;
import org.poo.bank.exchange.CurrencyGraph;
import org.poo.fileio.CommandInput;

import java.util.List;

public class CashWithdrawal implements Command {
    private final CommandInput command;
    private final List<User> users;
    private final CurrencyGraph currencyGraph;
    private final ArrayNode output;

    public CashWithdrawal(final CommandInput command, final List<User> users,
                          final ArrayNode output, final CurrencyGraph currencyGraph) {
        this.command = command;
        this.users = users;
        this.output = output;
        this.currencyGraph = currencyGraph;
    }

    /**
     * withdraws funds from the account
     */
    @Override
    public void execute() {
        String cardNumber = command.getCardNumber();
        double amount = command.getAmount();
        User user = CommandsUtils.getUserAfterCardNumber(users, cardNumber);
        if (user == null) {
            user = CommandsUtils.getUserAfterEmail(users, command.getEmail());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("command", Constants.CASH_WITHDRAWAL);

        // if the user is not found, it will output an error message
        if (user == null) {
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("timestamp", command.getTimestamp());
            errorNode.put("description", Constants.USER_NOT_FOUND);
            rootNode.set("output", errorNode);
            rootNode.put("timestamp", command.getTimestamp());
            output.add(rootNode);
            return;
        }

        Account account = CommandsUtils.getAccountAfterCardNumber(user, cardNumber);
        if (account == null) {
            ObjectNode errorNode = CommandsUtils.computeErrorOutput(command);
            rootNode.set("output", errorNode);
            rootNode.put("timestamp", command.getTimestamp());
            output.add(rootNode);
            return;
        }

        if (account.getType().equals(Constants.BUSINESS_ACCOUNT)
                && !user.getEmail().equals(((BusinessAccount) account).getOwner().getEmail())) {
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("timestamp", command.getTimestamp());
            errorNode.put("description", Constants.USER_NOT_FOUND);
            rootNode.set("output", errorNode);
            rootNode.put("timestamp", command.getTimestamp());
            output.add(rootNode);
            return;
        } else if (!account.getType().equals(Constants.BUSINESS_ACCOUNT) &&
                !user.getEmail().equals(command.getEmail()) && command.getEmail() != null) {
            ObjectNode errorNode = CommandsUtils.computeErrorOutput(command);
            rootNode.set("output", errorNode);
            rootNode.put("timestamp", command.getTimestamp());
            output.add(rootNode);
            return;
        }

        Card card = CommandsUtils.getCardAfterNumber(account, cardNumber);

        if (card == null) {
            ObjectNode errorNode = CommandsUtils.computeErrorOutput(command);
            rootNode.set("output", errorNode);
            rootNode.put("timestamp", command.getTimestamp());
            output.add(rootNode);
            return;
        }

        // if the card is frozen, add a transaction and return
        if (card.getStatus().equals(Constants.FROZEN)) {
            Transaction transaction = new Transaction.Builder()
                    .setDescription(Constants.CARD_IS_FROZEN)
                    .setTimestamp(command.getTimestamp())
                    .build();

            account.getTransactions().add(transaction);
            return;
        }

        // verifies if the account has enough funds
        double newAmount = user.getServicePlan().calculateCommission(amount,
                currencyGraph, Constants.RON);
        int error = account.payOnline(newAmount, Constants.RON);

        if (error == 1) {
            Transaction transaction = new Transaction.Builder()
                    .setDescription(Constants.INSUFFICIENT_FUNDS)
                    .setTimestamp(command.getTimestamp())
                    .build();

            account.getTransactions().add(transaction);
            return;
        }

        Transaction transaction = new Transaction.Builder()
                .setDescription("Cash withdrawal of " + amount)
                .setTimestamp(command.getTimestamp())
                .setCardPayment(command.getAmount())
                .setCommerciant(command.getCommerciant())
                .build();

        account.getTransactions().add(transaction);

    }
}
