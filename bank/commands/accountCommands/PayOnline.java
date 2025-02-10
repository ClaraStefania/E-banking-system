package org.poo.bank.commands.accountCommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.poo.bank.Constants;
import org.poo.bank.User;
import org.poo.bank.CommandsUtils;
import org.poo.bank.Commerciant;
import org.poo.bank.Manager;
import org.poo.bank.Employee;
import org.poo.bank.Transaction;
import org.poo.bank.Command;
import org.poo.bank.account.Account;
import org.poo.bank.account.BusinessAccount;
import org.poo.bank.card.Card;
import org.poo.bank.cashbackPlan.NrOfTransactions;
import org.poo.bank.cashbackPlan.SpendingThreshold;
import org.poo.bank.exchange.CurrencyGraph;
import org.poo.bank.servicePlan.GoldPlan;
import org.poo.bank.servicePlan.ServicePlan;
import org.poo.fileio.CommandInput;
import org.poo.utils.Utils;

import java.util.List;
import java.util.Map;

public class PayOnline implements Command {
    private final CommandInput command;
    private final List<User> users;
    private final ArrayNode output;
    private final CurrencyGraph currencyGraph;
    private final List<Commerciant> commerciants;

    public PayOnline(final CommandInput command, final List<User> users, final ArrayNode output,
                     final CurrencyGraph currencyGraph, final List<Commerciant> commerciants) {
        this.command = command;
        this.users = users;
        this.output = output;
        this.currencyGraph = currencyGraph;
        this.commerciants = commerciants;
    }

    /**
     * adds an error to the output
     * @param commandInput the command to be checked
     */
    private void addErrorToOutput(final CommandInput commandInput) {
        ObjectNode errorNode = CommandsUtils.computeErrorOutput(commandInput);
        ObjectNode rootNode = new ObjectMapper().createObjectNode();
        rootNode.put("command", Constants.PAY_ONLINE);
        rootNode.set("output", errorNode);
        rootNode.put("timestamp", command.getTimestamp());
        output.add(rootNode);
    }

    /**
     * computes the transaction for each case and adds it to the account
     * @param account the account to be checked
     * @param user the user to be checked
     */
    public void upgradePlanTransactions(final Account account, final User user) {
        double exchangeRate;
        double exchangedAmount;
        if (account.getType().equals(Constants.BUSINESS_ACCOUNT)) {
            exchangeRate = currencyGraph.calculateExchangeRate(command.getCurrency(),
                    Constants.RON);
            exchangedAmount = command.getAmount() * exchangeRate;
            if (((BusinessAccount) account).getOwner().getServicePlan().getType().
                    equals(Constants.SILVER) && exchangedAmount >= Constants.LIMIT_300) {
                ((BusinessAccount) account).getOwner().setNrOfSilverPayments(
                        user.getNrOfSilverPayments() + 1);
                if (((BusinessAccount) account).getOwner().getNrOfSilverPayments()
                        == Constants.NR_OF_SILVER_PAYMENTS) {
                    ServicePlan gold = new GoldPlan();
                    ((BusinessAccount) account).getOwner().setServicePlan(gold);
                    Transaction transactionUpgrade = new Transaction.Builder()
                            .setDescription("Upgrade plan")
                            .setNewPlanType(Constants.GOLD)
                            .setAccountIban(account.getIban())
                            .setTimestamp(command.getTimestamp())
                            .build();
                    account.getTransactions().add(transactionUpgrade);
                }
            }
        } else {
            exchangeRate = currencyGraph.calculateExchangeRate(command.getCurrency(),
                    Constants.RON);
            exchangedAmount = command.getAmount() * exchangeRate;
            if (user.getServicePlan().getType().equals(Constants.SILVER)
                    && exchangedAmount >= Constants.LIMIT_300) {
                user.setNrOfSilverPayments(user.getNrOfSilverPayments() + 1);
                if (user.getNrOfSilverPayments() == Constants.NR_OF_SILVER_PAYMENTS) {
                    ServicePlan gold = new GoldPlan();
                    user.setServicePlan(gold);
                    Transaction transactionUpgrade = new Transaction.Builder()
                            .setDescription("Upgrade plan")
                            .setNewPlanType(Constants.GOLD)
                            .setAccountIban(account.getIban())
                            .setTimestamp(command.getTimestamp())
                            .build();
                    account.getTransactions().add(transactionUpgrade);
                }
            }
        }
    }

    /**
     * computes the cashback for each case and adds it to the account
     * @param commerciant the commerciant to be checked
     * @param account the account to be checked
     * @param servicePlan the service plan to be checked
     */
    public void receiveCashback(final Commerciant commerciant, final Account account,
                                final ServicePlan servicePlan) {
        double cashback = 0;
        Map<String, NrOfTransactions> nrOfTransactionsMap = account.getNrOfTransactionsMap();
        NrOfTransactions nrOfTransactions = nrOfTransactionsMap.get(
                commerciant.getCommerciantName());
        if (commerciant.getCashbackStrategy().equals("spendingThreshold")) {
            // if the cashback strategy is the spending threshold
            Map<String, SpendingThreshold> spendingThresholdMap =
                    account.getSpendingThresholdMap();
            SpendingThreshold spendingThreshold = spendingThresholdMap.get(commerciant.getType());
            spendingThreshold.setAmountPaid(spendingThreshold.getAmountPaid()
                    + command.getAmount());

            double exchangeRate = currencyGraph.calculateExchangeRate(command.getCurrency(),
                    Constants.RON);
            double amountInRon = command.getAmount() * exchangeRate;

            // verifies if the cashback should be received
            account.setAmountSpendingThreshold(account.getAmountSpendingThreshold() + amountInRon);
            if ((account.getCashbackToReceiveClothes() == 1
                    && nrOfTransactions.getType().equals("Clothes")
                    || account.getCashbackToReceiveFood() == 1
                    && nrOfTransactions.getType().equals("Food")
                    || account.getCashbackToReceiveTech() == 1
                    && nrOfTransactions.getType().equals("Tech"))) {
                cashback = nrOfTransactions.calculateCashback(command.getAmount(),
                        account.getNrOfTransactionsMap());
                account.setCashbackToReceiveFood(0);
                account.setCashbackToReceiveClothes(0);
                account.setCashbackToReceiveTech(0);
            }
            cashback += spendingThreshold.calculateCashback(command.getAmount(),
                    servicePlan, account.getAmountSpendingThreshold(), currencyGraph,
                    Constants.RON);
        } else {
            // if the cashback strategy is the number of transactions
            if ((account.getCashbackToReceiveClothes() == 1
                    && nrOfTransactions.getType().equals("Clothes")
                    || account.getCashbackToReceiveFood() == 1
                    && nrOfTransactions.getType().equals("Food")
                    || account.getCashbackToReceiveTech() == 1
                    && nrOfTransactions.getType().equals("Tech"))) {
                cashback = nrOfTransactions.calculateCashback(command.getAmount(),
                        account.getNrOfTransactionsMap());
            }
            nrOfTransactions.setNrOfTransactions(nrOfTransactions.getNrOfTransactions() + 1);

            if (nrOfTransactions.getNrOfTransactions()
                    == nrOfTransactions.getStandardNrOfTransactions()) {
                switch (nrOfTransactions.getType()) {
                    case "Food" -> account.setCashbackToReceiveFood(1);
                    case "Clothes" -> account.setCashbackToReceiveClothes(1);
                    case "Tech" -> account.setCashbackToReceiveTech(1);
                    default -> {
                    }
                }
            }
        }

        account.acceptPayment(cashback, command.getCurrency());
    }

    /**
     * adds the spent amount to the associate
     * @param account the account to be checked
     * @param user the user to be checked
     * @param commerciantName the name of the commerciant
     */
    public void updateTheAssociatesAmount(final Account account, final User user,
                                          final String commerciantName) {
        if (account.getType().equals(Constants.BUSINESS_ACCOUNT)) {
            double exchangeRate = currencyGraph.calculateExchangeRate(command.getCurrency(),
                    account.getCurrency());
            double exchangedAmount = command.getAmount() * exchangeRate;

            Commerciant commerciantReceiver = CommandsUtils.verifyIfCommerciantExists(
                    ((BusinessAccount) account).getCommerciants(), commerciantName);
            if (commerciantReceiver == null && !user.getEmail().equals(
                    ((BusinessAccount) account).getOwner().getEmail())) {
                commerciantReceiver = new Commerciant(commerciantName,
                        exchangedAmount, 0, null, "", "");
                commerciantReceiver.addCommerciantAlphabetically(
                        ((BusinessAccount) account).getCommerciants(), commerciantReceiver);
            } else if (!user.getEmail().equals(
                    ((BusinessAccount) account).getOwner().getEmail())) {
                commerciantReceiver.setAmount(commerciantReceiver.getAmount() + exchangedAmount);
            }

            if (!user.getEmail().equals(((BusinessAccount) account).getOwner().getEmail())) {
                Manager manager = CommandsUtils.verifyIfUserIsManager(
                        ((BusinessAccount) account).getManagers(), user.getEmail());
                Employee employee = CommandsUtils.verifyIfUserIsEmployee(
                        ((BusinessAccount) account).getEmployees(), user.getEmail());
                if (manager != null) {
                    commerciantReceiver.getManagers().add(manager);
                } else if (employee != null) {
                    commerciantReceiver.getEmployees().add(employee);
                }
            }
        }
    }

    /**
     * pays with the card
     * computes the transaction for each case and adds it to the account
     */
    @Override
    public void execute() {
        if (command.getAmount() == 0) {
            return;
        }

        String cardNumber = command.getCardNumber();
        User user = CommandsUtils.getUserAfterEmail(users, command.getEmail());

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("command", Constants.PAY_ONLINE);

        if (user == null) {
            return;
        }

        Card card = CommandsUtils.getCardAfterCardNumber(user.getAccounts(), cardNumber);
        if (card == null) {
            addErrorToOutput(command);
            return;
        }

        Account account = CommandsUtils.getAccountAfterCardNumber(user, cardNumber);
        if (account == null) {
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
        ServicePlan servicePlan;

        // verifies if the account has enough funds
        if (account.getType().equals(Constants.BUSINESS_ACCOUNT)) {
            Employee employee = CommandsUtils.verifyIfUserIsEmployee(
                    ((BusinessAccount) account).getEmployees(), user.getEmail());
            if (CommandsUtils.userIsAssociate((BusinessAccount) account, user) == 0) {
                return;
            }

            servicePlan = ((BusinessAccount) account).getOwner().getServicePlan();
            double exchangeRate = currencyGraph.calculateExchangeRate(command.getCurrency(),
                    account.getCurrency());
            double exchangedAmount = command.getAmount() * exchangeRate;
            double paymentLimit = ((BusinessAccount) account).getPaymentLimit();
            if (exchangedAmount > paymentLimit && employee != null) {
                return;
            }
        } else {
            servicePlan = user.getServicePlan();
        }

        double newAmount = servicePlan.calculateCommission(command.getAmount(),
                currencyGraph, command.getCurrency());
        int error = account.payOnline(newAmount, command.getCurrency());

        if (error == 1) {
            Transaction transaction = new Transaction.Builder()
                    .setDescription(Constants.INSUFFICIENT_FUNDS)
                    .setTimestamp(command.getTimestamp())
                    .build();

            account.getTransactions().add(transaction);
            return;
        }

        // verifies if the user is an associate
        if (account.getType().equals(Constants.BUSINESS_ACCOUNT)
                && CommandsUtils.userIsAssociate((BusinessAccount) account, user) == 0) {
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("timestamp", command.getTimestamp());
            errorNode.put("description", Constants.USER_NOT_FOUND);
            rootNode.set("output", errorNode);
            rootNode.put("timestamp", command.getTimestamp());
            output.add(rootNode);
            return;
        } else if (!account.getType().equals(Constants.BUSINESS_ACCOUNT)
                && !user.getEmail().equals(command.getEmail()) && command.getEmail() != null) {
            addErrorToOutput(command);
            return;
        }

        // verifies if the payment is made to a commerciant
        String commerciantName = command.getCommerciant();
        if (commerciantName != null) {
            Commerciant commerciant = CommandsUtils.getCommerciantType(commerciants,
                    commerciantName);

            receiveCashback(commerciant, account, servicePlan);
            updateTheAssociatesAmount(account, user, commerciantName);
        }

        double exchangeRate = currencyGraph.calculateExchangeRate(command.getCurrency(),
                account.getCurrency());
        double exchangedAmount = command.getAmount() * exchangeRate;
        Transaction transaction = new Transaction.Builder()
                .setDescription("Card payment")
                .setTimestamp(command.getTimestamp())
                .setCardPayment(exchangedAmount)
                .setCommerciant(command.getCommerciant())
                .build();

        account.getTransactions().add(transaction);

        if (account.getType().equals(Constants.BUSINESS_ACCOUNT)) {
            Manager manager = CommandsUtils.verifyIfUserIsManager(
                    ((BusinessAccount) account).getManagers(), user.getEmail());
            Employee employee = CommandsUtils.verifyIfUserIsEmployee(
                    ((BusinessAccount) account).getEmployees(), user.getEmail());
            exchangeRate = currencyGraph.calculateExchangeRate(command.getCurrency(),
                    account.getCurrency());
            exchangedAmount = command.getAmount() * exchangeRate;

            if (manager != null) {
                manager.setSpent(manager.getSpent() + exchangedAmount);
            } else if (employee != null) {
                if (exchangedAmount > ((BusinessAccount) account).getPaymentLimit()) {
                    return;
                }
                employee.setSpent(employee.getSpent() + exchangedAmount);
            }
        }

        upgradePlanTransactions(account, user);

        // if the card is a one-time-pay card, create a new card
        if (card.getType().equals(Constants.ONE_TIME_PAY_CARD)) {
            Transaction newTransaction = new Transaction.Builder()
                    .setTimestamp(command.getTimestamp())
                    .setDescription(Constants.CARD_DESTROYED)
                    .setCardNumber(card.getCardNumber())
                    .setCardHolder(user.getEmail())
                    .setAccount(account.getIban())
                    .build();

            card.setCardNumber(Utils.generateCardNumber());

            Transaction newCardTransaction = new Transaction.Builder()
                    .setTimestamp(command.getTimestamp())
                    .setDescription("New card created")
                    .setCardNumber(card.getCardNumber())
                    .setCardHolder(user.getEmail())
                    .setAccount(account.getIban())
                    .build();
            account.getTransactions().add(newTransaction);
            account.getTransactions().add(newCardTransaction);
        }
    }
}
