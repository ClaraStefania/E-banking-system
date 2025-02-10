package org.poo.bank.commands.accountCommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Constants;
import org.poo.bank.User;
import org.poo.bank.CommandsUtils;
import org.poo.bank.Transaction;
import org.poo.bank.SplitPaymentClass;
import org.poo.bank.Command;
import org.poo.bank.account.Account;
import org.poo.bank.exchange.CurrencyGraph;
import org.poo.fileio.CommandInput;

import java.util.List;

public class RespondToSplitPayment implements Command {
    private final CommandInput command;
    private final List<User> users;
    private final List<SplitPaymentClass> splitPayments;
    private final CurrencyGraph currencyGraph;
    private final ArrayNode output;

    public RespondToSplitPayment(final CommandInput command, final List<User> users,
                                 final List<SplitPaymentClass> splitPayments,
                                 final CurrencyGraph currencyGraph, final ArrayNode output) {
        this.command = command;
        this.users = users;
        this.splitPayments = splitPayments;
        this.currencyGraph = currencyGraph;
        this.output = output;
    }

    /**
     * accepts the payment
     * @param implicatedUsers the users that are implicated in the split payment
     * @param email the email of the user that accepts the payment
     * @param acceptedPayments the list of accepted payments
     */
    public void acceptPayment(final List<User> implicatedUsers, final String email,
                              final List<Integer> acceptedPayments) {
        for (int i = 0; i < implicatedUsers.size(); i++) {
            User user = implicatedUsers.get(i);
            if (user.getEmail().equals(email)) {
                acceptedPayments.set(i, 1);
                return;
            }
        }
    }

    /**
     * rejects the payment
     * @param implicatedUsers the users that are implicated in the split payment
     * @param email the email of the user that rejects the payment
     * @param acceptedPayments the list of accepted payments
     */
    public void rejectPayment(final List<User> implicatedUsers, final String email,
                              final List<Integer> acceptedPayments) {
        for (int i = 0; i < implicatedUsers.size(); i++) {
            User user = implicatedUsers.get(i);
            if (user.getEmail().equals(email)) {
                acceptedPayments.set(i, 2);
                return;
            }
        }
    }

    /**
     * responds to the split payment
     */
    @Override
    public void execute() {
        ObjectMapper objectMapper = new ObjectMapper();
        String email = command.getEmail();
        User user;

        if (splitPayments.isEmpty()) {
            return;
        }

        user = CommandsUtils.getUserAfterEmail(users, email);
        if (user == null) {
            ObjectNode rootNode = objectMapper.createObjectNode();
            rootNode.put("command", command.getCommand());
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("timestamp", command.getTimestamp());
            errorNode.put("description", Constants.USER_NOT_FOUND);
            rootNode.set("output", errorNode);
            rootNode.put("timestamp", command.getTimestamp());
            output.add(rootNode);
            return;
        }

        // find the split payment that the user has to respond to
        SplitPaymentClass splitPaymentToAccept = null;
        for (SplitPaymentClass splitPayment : splitPayments) {
            if (!splitPayment.getType().equals(command.getSplitPaymentType())) {
                continue;
            }
            List<User> implicatedUsers = splitPayment.getImplicatedUsers();
            user = CommandsUtils.getUserAfterEmail(implicatedUsers, email);
            if (user != null && splitPayment.verifyIfUserAccepted(user) == 0) {
                splitPaymentToAccept = splitPayment;
                break;
            }
        }

        if (splitPaymentToAccept == null) {
            return;
        }

        if (command.getCommand().equals(Constants.ACCEPT_SPLIT_PAYMENT)) {
            acceptPayment(splitPaymentToAccept.getImplicatedUsers(), email,
                    splitPaymentToAccept.getAcceptedPayments());
        } else {
            rejectPayment(splitPaymentToAccept.getImplicatedUsers(), email,
                    splitPaymentToAccept.getAcceptedPayments());
        }

        if (splitPaymentToAccept.verifyIfAllUsersResponded() == 0) {
            return;
        }

        // if all users responded, the payment will be processed
        splitPayments.remove(splitPaymentToAccept);
        List<Account> validAccounts = splitPaymentToAccept.getValidAccounts();
        List<Double> amountsForUsers = splitPaymentToAccept.getAmountsForUsers();
        String accountNoFounds;

        // if one user rejected the payment, the payment will not be processed
        if (splitPaymentToAccept.verifyIfSomeoneRejected() == 1) {
            splitPayments.remove(splitPaymentToAccept);
            Transaction transaction;
            String displayAmount = String.format("%.2f", splitPaymentToAccept.getAmount());
            if (splitPaymentToAccept.getType().equals("equal")) {
                double amountToPay = amountsForUsers.getFirst();
                transaction = new Transaction.Builder()
                        .setTimestamp(splitPaymentToAccept.getTimestamp())
                        .setDescription("Split payment of " + displayAmount + " "
                                + splitPaymentToAccept.getCurrency())
                        .setError("One user rejected the payment.")
                        .setSplitCurrency(splitPaymentToAccept.getCurrency())
                        .setSplitPaymentType(splitPaymentToAccept.getType())
                        .setAmountEqualSplitPayment(amountToPay)
                        .setInvolvedAccounts(splitPaymentToAccept.getIbans())
                        .build();
            } else {
                transaction = new Transaction.Builder()
                        .setTimestamp(splitPaymentToAccept.getTimestamp())
                        .setDescription("Split payment of " + displayAmount + " "
                                + splitPaymentToAccept.getCurrency())
                        .setError("One user rejected the payment.")
                        .setSplitCurrency(splitPaymentToAccept.getCurrency())
                        .setSplitPaymentType(splitPaymentToAccept.getType())
                        .setAmountForUsers(amountsForUsers)
                        .setInvolvedAccounts(splitPaymentToAccept.getIbans())
                        .build();
            }
            for (Account validAccount : validAccounts) {
                validAccount.getTransactions().add(transaction);
            }
            return;
        }

        // verify if the users have enough funds
        for (int i = 0; i < validAccounts.size(); i++) {
            Account account = validAccounts.get(i);
            double amountToPay = amountsForUsers.get(i);
            double exchangedAmount = amountToPay
                    * currencyGraph.calculateExchangeRate(splitPaymentToAccept.getCurrency(),
                    account.getCurrency());
            if (account.getBalance() >= exchangedAmount) {
                continue;
            }

            String displayAmount = String.format("%.2f", splitPaymentToAccept.getAmount());

            accountNoFounds = splitPaymentToAccept.getIbans().get(i);
            Transaction transaction;

            if (splitPaymentToAccept.getType().equals("equal")) {
                transaction = new Transaction.Builder()
                        .setTimestamp(splitPaymentToAccept.getTimestamp())
                        .setDescription("Split payment of " + displayAmount + " "
                                + splitPaymentToAccept.getCurrency())
                        .setError("Account " + accountNoFounds + " has insufficient funds"
                                + " for a split payment.")
                        .setSplitCurrency(splitPaymentToAccept.getCurrency())
                        .setSplitPaymentType(splitPaymentToAccept.getType())
                        .setAmountEqualSplitPayment(amountToPay)
                        .setInvolvedAccounts(splitPaymentToAccept.getIbans())
                        .build();
            } else {
                transaction = new Transaction.Builder()
                        .setTimestamp(splitPaymentToAccept.getTimestamp())
                        .setDescription("Split payment of " + displayAmount + " "
                                + splitPaymentToAccept.getCurrency())
                        .setError("Account " + accountNoFounds + " has insufficient funds"
                                + " for a split payment.")
                        .setSplitCurrency(splitPaymentToAccept.getCurrency())
                        .setSplitPaymentType(splitPaymentToAccept.getType())
                        .setAmountForUsers(amountsForUsers)
                        .setInvolvedAccounts(splitPaymentToAccept.getIbans())
                        .build();
            }
            for (Account validAccount : validAccounts) {
                validAccount.getTransactions().add(transaction);
            }
            return;
        }

        // if all accounts have enough funds, the payment will be processed
        String displayAmount = String.format("%.2f", splitPaymentToAccept.getAmount());
        Transaction transaction;
        if (splitPaymentToAccept.getType().equals("equal")) {
            transaction = new Transaction.Builder()
                    .setTimestamp(splitPaymentToAccept.getTimestamp())
                    .setDescription("Split payment of " + displayAmount + " "
                            + splitPaymentToAccept.getCurrency())
                    .setSplitCurrency(splitPaymentToAccept.getCurrency())
                    .setSplitPaymentType(splitPaymentToAccept.getType())
                    .setAmountEqualSplitPayment(amountsForUsers.getFirst())
                    .setInvolvedAccounts(splitPaymentToAccept.getIbans())
                    .build();
        } else {
            transaction = new Transaction.Builder()
                    .setTimestamp(splitPaymentToAccept.getTimestamp())
                    .setDescription("Split payment of " + displayAmount + " "
                            + splitPaymentToAccept.getCurrency())
                    .setSplitCurrency(splitPaymentToAccept.getCurrency())
                    .setSplitPaymentType(splitPaymentToAccept.getType())
                    .setAmountForUsers(amountsForUsers)
                    .setInvolvedAccounts(splitPaymentToAccept.getIbans())
                    .build();
        }
        splitPaymentToAccept.payAllAccounts();
        for (Account account : validAccounts) {
            account.getTransactions().add(transaction);
        }
    }
}
