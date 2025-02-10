package org.poo.bank;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.commands.accountCommands.DeleteAccount;
import org.poo.bank.commands.userCommands.PrintUsers;
import org.poo.bank.commands.accountCommands.AddAccount;
import org.poo.bank.commands.accountCommands.AddFunds;
import org.poo.bank.commands.cardCommands.DeleteCard;
import org.poo.bank.commands.cardCommands.CreateCard;
import org.poo.bank.commands.accountCommands.PayOnline;
import org.poo.bank.commands.accountCommands.SendMoney;
import org.poo.bank.commands.accountCommands.PrintTransactions;
import org.poo.bank.commands.cardCommands.CheckCardStatus;
import org.poo.bank.commands.accountCommands.SetMinimumBalance;
import org.poo.bank.commands.accountCommands.SplitPayment;
import org.poo.bank.commands.accountCommands.Report;
import org.poo.bank.commands.accountCommands.SpendingsReport;
import org.poo.bank.commands.accountCommands.ChangeInterestRate;
import org.poo.bank.commands.accountCommands.AddInterest;
import org.poo.bank.commands.accountCommands.WithdrawSavings;
import org.poo.bank.commands.accountCommands.ChangeDepositLimit;
import org.poo.bank.commands.accountCommands.UpgradePlan;
import org.poo.bank.commands.accountCommands.CashWithdrawal;
import org.poo.bank.commands.accountCommands.RespondToSplitPayment;
import org.poo.bank.commands.accountCommands.AddNewBusinessAssociate;
import org.poo.bank.commands.accountCommands.ChangeSpendingLimit;
import org.poo.bank.commands.accountCommands.BusinessReport;
import org.poo.bank.commands.userCommands.SetAlias;
import org.poo.bank.exchange.CurrencyGraph;
import org.poo.fileio.CommandInput;
import org.poo.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CommandsManagement {
    private static CommandsManagement singleInstance = null;
    private final CommandInvoker invoker = new CommandInvoker();
    private final Map<String, String> aliases = new HashMap<>();

    private CommandsManagement() {

    }

    /**
     * gets the instance of the commands management
     * @return the instance of the commands management
     */
    public static CommandsManagement getInstance() {
        if (singleInstance == null) {
            singleInstance = new CommandsManagement();
        }
        return singleInstance;
    }

    /**
     * creates the commands depending on the input and executes them
     * @param commands the commands to be executed
     * @param users the list of users
     * @param output the output array
     * @param currencyGraph the currency graph
     */
    public void manageCommands(final CommandInput[] commands, final List<User> users,
                               final ArrayNode output, final CurrencyGraph currencyGraph,
                               final List<Commerciant> commerciants,
                               final List<SplitPaymentClass> splitPayments) {
        Utils.resetRandom();
        for (CommandInput command : commands) {
            Command concreteCommand = null;
            switch (command.getCommand()) {
                case Constants.PRINT_USERS:
                    concreteCommand = new PrintUsers(command, users, output);
                    break;
                case Constants.ADD_ACCOUNT:
                    concreteCommand = new AddAccount(command, users, currencyGraph, commerciants);
                    break;
                case Constants.CREATE_CARD:
                    concreteCommand = new CreateCard(command, users, Constants.CLASSIC_CARD);
                    break;
                case Constants.ADD_FUNDS:
                    concreteCommand = new AddFunds(command, users);
                    break;
                case Constants.DELETE_ACCOUNT:
                    concreteCommand = new DeleteAccount(command, users, output);
                    break;
                case Constants.DELETE_CARD:
                    concreteCommand = new DeleteCard(command, users);
                    break;
                case Constants.CREATE_ONE_TIME_CARD:
                    concreteCommand = new CreateCard(command, users, Constants.ONE_TIME_PAY_CARD);
                    break;
                case Constants.PAY_ONLINE:
                    concreteCommand = new PayOnline(command, users, output,
                            currencyGraph, commerciants);
                    break;
                case Constants.SEND_MONEY:
                    concreteCommand = new SendMoney(command, users, aliases, currencyGraph,
                            output, commerciants);
                    break;
                case Constants.PRINT_TRANSACTIONS:
                    concreteCommand = new PrintTransactions(command, users, output);
                    break;
                case Constants.CHECK_CARD_STATUS:
                    concreteCommand = new CheckCardStatus(command, users, output);
                    break;
                case Constants.SET_MINIMUM_BALANCE:
                    concreteCommand = new SetMinimumBalance(command, users);
                    break;
                case Constants.SPLIT_PAYMENT:
                    concreteCommand = new SplitPayment(command, users, splitPayments);
                    break;
                case Constants.REPORT:
                    concreteCommand = new Report(command, users, output);
                    break;
                case Constants.SPENDINGS_REPORT:
                    concreteCommand = new SpendingsReport(command, users, output);
                    break;
                case Constants.CHANGE_INTEREST_RATE:
                    concreteCommand = new ChangeInterestRate(command, users, output);
                    break;
                case Constants.ADD_INTEREST:
                    concreteCommand = new AddInterest(command, users, output);
                    break;
                case Constants.SET_ALIAS:
                    concreteCommand = new SetAlias(command, aliases);
                    break;
                case Constants.WITHDRAW_SAVINGS:
                    concreteCommand = new WithdrawSavings(command, users);
                    break;
                case Constants.UPGRADE_PLAN:
                    concreteCommand = new UpgradePlan(command, users, output);
                    break;
                case Constants.CASH_WITHDRAWAL:
                    concreteCommand = new CashWithdrawal(command, users, output, currencyGraph);
                    break;
                case Constants.ACCEPT_SPLIT_PAYMENT, Constants.REJECT_SPLIT_PAYMENT:
                    concreteCommand = new RespondToSplitPayment(command, users, splitPayments,
                            currencyGraph, output);
                    break;
                case Constants.ADD_NEW_BUSINESS_ASSOCIATE:
                    concreteCommand = new AddNewBusinessAssociate(command, users);
                    break;
                case Constants.CHANGE_SPENDING_LIMIT:
                    concreteCommand = new ChangeSpendingLimit(command, users, output);
                    break;
                case Constants.BUSINESS_REPORT:
                    concreteCommand = new BusinessReport(command, users, output);
                    break;
                case Constants.CHANGE_DEPOSIT_LIMIT:
                    concreteCommand = new ChangeDepositLimit(command, users, output);
                    break;
                default:
                    break;
            }
            if (concreteCommand != null) {
                invoker.setCommand(concreteCommand);
                invoker.executeCommand();
            }
        }
    }
}
