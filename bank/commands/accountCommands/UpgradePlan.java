package org.poo.bank.commands.accountCommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Constants;
import org.poo.bank.User;
import org.poo.bank.CommandsUtils;
import org.poo.bank.Transaction;
import org.poo.bank.Command;
import org.poo.bank.account.Account;
import org.poo.bank.servicePlan.ServicePlan;
import org.poo.fileio.CommandInput;

import java.util.List;

public class UpgradePlan implements Command {
    private final CommandInput command;
    private final List<User> users;
    private final ArrayNode output;

    public UpgradePlan(final CommandInput command, final List<User> users,
                       final ArrayNode output) {
        this.command = command;
        this.users = users;
        this.output = output;
    }

    /**
     * upgrades the plan of the user and computes the transaction
     */
    @Override
    public void execute() {
        ObjectMapper objectMapper = new ObjectMapper();
        String iban = command.getAccount();
        String newType = command.getNewPlanType();
        Account account = CommandsUtils.getAccountAfterIban(users, iban);
        User user = CommandsUtils.getUserAfterIban(users, iban);

        if (account == null) {
            ObjectNode rootNode = objectMapper.createObjectNode();
            rootNode.put("command", Constants.UPGRADE_PLAN);
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("timestamp", command.getTimestamp());
            errorNode.put("description", Constants.ACCOUNT_NOT_FOUND);
            rootNode.set("output", errorNode);
            rootNode.put("timestamp", command.getTimestamp());
            output.add(rootNode);
            return;
        }

        if (user == null) {
            return;
        }

        double amountBeforeUpgrade = account.getBalance();

        ServicePlan servicePlan = user.getServicePlan();

        if (newType.equals(Constants.STUDENT) || newType.equals(Constants.STANDARD)) {
            return;
        }

        if (servicePlan.getType().equals(Constants.GOLD) && newType.equals(Constants.SILVER)) {
            return;
        }

        if (servicePlan.getType().equals(newType)) {
            Transaction transaction = new Transaction.Builder()
                    .setDescription("The user already has the " + newType + " plan.")
                    .setTimestamp(command.getTimestamp())
                    .build();
            account.getTransactions().add(transaction);
            return;
        }

        // verifies if the account has funds
        if (amountBeforeUpgrade == 0) {
            Transaction transaction = new Transaction.Builder()
                    .setDescription(Constants.INSUFFICIENT_FUNDS)
                    .setTimestamp(command.getTimestamp())
                    .build();
            account.getTransactions().add(transaction);
            return;
        }

        ServicePlan newServicePlan = user.getServicePlanFactory().createServiceplan(newType);
        newServicePlan.payFee(account,servicePlan.getType());
        user.setServicePlan(newServicePlan);

        if (account.getBalance() == amountBeforeUpgrade) {
            Transaction transaction = new Transaction.Builder()
                    .setDescription(Constants.INSUFFICIENT_FUNDS)
                    .setTimestamp(command.getTimestamp())
                    .build();
            account.getTransactions().add(transaction);
            return;
        }

        Transaction transaction = new Transaction.Builder()
                .setDescription("Upgrade plan")
                .setAccountIban(iban)
                .setNewPlanType(newType)
                .setTimestamp(command.getTimestamp())
                .build();
        account.getTransactions().add(transaction);
    }
}
