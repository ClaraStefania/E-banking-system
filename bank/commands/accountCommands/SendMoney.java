package org.poo.bank.commands.accountCommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Employee;
import org.poo.bank.Manager;
import org.poo.bank.Commerciant;
import org.poo.bank.Constants;
import org.poo.bank.User;
import org.poo.bank.CommandsUtils;
import org.poo.bank.Transaction;
import org.poo.bank.Command;
import org.poo.bank.account.Account;
import org.poo.bank.account.BusinessAccount;
import org.poo.bank.exchange.CurrencyGraph;
import org.poo.bank.servicePlan.ServicePlan;
import org.poo.fileio.CommandInput;

import java.util.List;
import java.util.Map;

public class SendMoney implements Command {
    private final CommandInput command;
    private final List<User> users;
    private final Map<String, String> aliases;
    private CurrencyGraph currencyGraph;
    private ArrayNode output;
    private List<Commerciant> commerciants;

    public SendMoney(final CommandInput command, final List<User> users,
                     final Map<String, String> aliases, final CurrencyGraph currencyGraph,
                     final ArrayNode output, final List<Commerciant> commerciants) {
        this.command = command;
        this.users = users;
        this.aliases = aliases;
        this.currencyGraph = currencyGraph;
        this.output = output;
        this.commerciants = commerciants;
    }

    /**
     * sends money from one account to another
     * if the receiver is an alias, it will be replaced with the actual iban
     */
    @Override
    public void execute() {
        String sourceIban = command.getAccount();
        String destinationIban = command.getReceiver();
        int commerciantAccount = 0;
        User sourceUser = CommandsUtils.getUserAfterEmail(users, command.getEmail());
        Commerciant commerciant = null;
        PayOnline payOnlineInstance = new PayOnline(command, users, output,
                currencyGraph, commerciants);

        if (sourceUser == null) {
            return;
        }

        if (aliases.containsKey(destinationIban)) {
            destinationIban = aliases.get(destinationIban);
        }

        Account sourceAccount = CommandsUtils.verifyIfAccountExists(sourceUser.getAccounts(),
                sourceIban);
        if (sourceAccount == null) {
            return;
        }

        // verify if the sender is an associate
        if (sourceAccount.getType().equals(Constants.BUSINESS_ACCOUNT)) {
            if (CommandsUtils.userIsAssociate((BusinessAccount) sourceAccount, sourceUser) == 0) {
                return;
            }
        } else if (!sourceAccount.getType().equals(Constants.BUSINESS_ACCOUNT)
                && !sourceUser.getEmail().equals(command.getEmail())
                && command.getEmail() != null) {
            return;
        }

        Account destinationAccount = CommandsUtils.verifyIfIsAnUserAccount(users, destinationIban);
        if (destinationAccount == null) {
            destinationAccount = CommandsUtils.verifyIfIsACommerciantAccount(commerciants,
                    destinationIban);
            commerciantAccount = 1;
            commerciant = CommandsUtils.getCommerciantAfterAccount(commerciants, destinationIban);
        }

        if (destinationAccount == null) {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode rootNode = objectMapper.createObjectNode();
            rootNode.put("command", command.getCommand());
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("timestamp", command.getTimestamp());
            errorNode.put("description", "User not found");
            rootNode.set("output", errorNode);
            rootNode.put("timestamp", command.getTimestamp());
            output.add(rootNode);
            return;
        }
        User user = CommandsUtils.getUserAfterIban(users, destinationIban);
        ServicePlan servicePlan;

        // get the service plan of the sender
        if (sourceAccount.getType().equals(Constants.BUSINESS_ACCOUNT)) {
            servicePlan = ((BusinessAccount) sourceAccount).getOwner().getServicePlan();
            double exchangeRate = currencyGraph.calculateExchangeRate(command.getCurrency(),
                    sourceAccount.getCurrency());
            double exchangedAmount = command.getAmount() * exchangeRate;
            if (exchangedAmount > ((BusinessAccount) sourceAccount).getPaymentLimit()
                   && CommandsUtils.verifyIfUserIsEmployee(
                            ((BusinessAccount) sourceAccount).getEmployees(),
                            sourceUser.getEmail()) != null) {
                return;
            }
        } else {
            servicePlan = sourceUser.getServicePlan();
        }

        double newAmount = servicePlan.calculateCommission(command.getAmount(),
                currencyGraph, sourceAccount.getCurrency());
        int error = sourceAccount.payOnline(newAmount, sourceAccount.getCurrency());

        // if the sender does not have enough funds, add an error transaction
        if (error == 1) {
            if (sourceAccount.getType().equals(Constants.BUSINESS_ACCOUNT)
                    && sourceUser.getEmail().equals(
                            ((BusinessAccount) sourceAccount).getOwner().getEmail())) {
                return;
            }
            Transaction transaction = new Transaction.Builder()
                    .setAmount(-1)
                    .setDescription(Constants.INSUFFICIENT_FUNDS)
                    .setTimestamp(command.getTimestamp())
                    .build();

            sourceAccount.getTransactions().add(transaction);
            return;
        }

        // if the receiver is a commerciant, calculates the cashback
        if (commerciantAccount == 1) {
            payOnlineInstance.receiveCashback(commerciant, sourceAccount, servicePlan);
        }

        double payment = destinationAccount.acceptPayment(command.getAmount(),
                sourceAccount.getCurrency());
        if (payment == 0) {
            return;
        }
        double exchangeRate;
        double exchangedAmount;

        if (sourceAccount.getType().equals(Constants.BUSINESS_ACCOUNT)) {
            Manager manager = CommandsUtils.verifyIfUserIsManager(
                    ((BusinessAccount) sourceAccount).getManagers(), sourceUser.getEmail());
            Employee employee = CommandsUtils.verifyIfUserIsEmployee(
                    ((BusinessAccount) sourceAccount).getEmployees(), sourceUser.getEmail());
            exchangeRate = currencyGraph.calculateExchangeRate(command.getCurrency(),
                    sourceAccount.getCurrency());
            exchangedAmount = command.getAmount() * exchangeRate;

            if (manager != null) {
                manager.setSpent(manager.getSpent() + exchangedAmount);
            } else if (employee != null) {
                if (exchangedAmount > ((BusinessAccount) sourceAccount).getPaymentLimit()) {
                    return;
                }
                employee.setSpent(employee.getSpent() + exchangedAmount);
            }

            payOnlineInstance.upgradePlanTransactions(sourceAccount, sourceUser);
        }


        if (commerciantAccount == 0) {
            // create transactions for both accounts
            Transaction transactionReceiver = new Transaction.Builder()
                    .setAmount(payment)
                    .setSenderIban(sourceIban)
                    .setReceiverIban(destinationIban)
                    .setTimestamp(command.getTimestamp())
                    .setDescription(command.getDescription())
                    .setFirstName(user.getFirstName())
                    .setLastName(user.getLastName())
                    .setCurrency(destinationAccount.getCurrency())
                    .setTransferType("received")
                    .build();

            destinationAccount.getTransactions().add(transactionReceiver);

            if (sourceAccount.getType().equals(Constants.BUSINESS_ACCOUNT)
                    && sourceUser.getEmail().equals(
                            ((BusinessAccount) sourceAccount).getOwner().getEmail())) {
                return;
            }

            Transaction transactionSender = new Transaction.Builder()
                    .setAmount(command.getAmount())
                    .setSenderIban(sourceIban)
                    .setReceiverIban(destinationIban)
                    .setTimestamp(command.getTimestamp())
                    .setDescription(command.getDescription())
                    .setFirstName(user.getFirstName())
                    .setLastName(user.getLastName())
                    .setCurrency(sourceAccount.getCurrency())
                    .setTransferType("sent")
                    .build();

            sourceAccount.getTransactions().add(transactionSender);
        } else {
            if (sourceAccount.getType().equals(Constants.BUSINESS_ACCOUNT)
                    && sourceUser.getEmail().equals((
                            (BusinessAccount) sourceAccount).getOwner().getEmail())) {
                return;
            }

            Transaction transaction = new Transaction.Builder()
                    .setAmount(command.getAmount())
                    .setSenderIban(sourceIban)
                    .setReceiverIban(destinationIban)
                    .setTimestamp(command.getTimestamp())
                    .setDescription(command.getDescription())
                    .setCurrency(sourceAccount.getCurrency())
                    .setTransferType("sent")
                    .build();
            sourceAccount.getTransactions().add(transaction);
        }
    }
}
