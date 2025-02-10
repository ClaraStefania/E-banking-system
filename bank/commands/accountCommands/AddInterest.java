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
import org.poo.bank.account.SavingsAccount;
import org.poo.fileio.CommandInput;

import java.util.List;

public class AddInterest implements Command {
    private final CommandInput command;
    private final List<User> users;
    private final ArrayNode output;
    private final ObjectMapper objectMapper;

    public AddInterest(final CommandInput command, final List<User> users,
                       final ArrayNode output) {
        this.command = command;
        this.users = users;
        this.output = output;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * adds interest to the account if it is a savings account
     * if the account is a classic account, it will compute the error output
     */
    @Override
    public void execute() {
        String iban = command.getAccount();
        Account account = CommandsUtils.getAccountAfterIban(users, iban);
        if (account == null) {
            return;
        }

        if (account.getType().equals(Constants.CLASSIC_ACCOUNT)) {
            ObjectNode rootNode =  account.computeErrorOutput(objectMapper, command);
            output.add(rootNode);
            Transaction transaction = new Transaction.Builder()
                    .setDescription("You do not have a savings account.")
                    .setTimestamp(command.getTimestamp())
                    .build();
            account.getTransactions().add(transaction);
            return;
        }

        double interestRate = ((SavingsAccount) account).getInterestRate();
        double interest = interestRate * account.getBalance();
        account.acceptPayment(interest, account.getCurrency());

        Transaction transaction = new Transaction.Builder()
                .setDescription("Interest rate income")
                .setCardPayment(interest)
                .setSplitCurrency(account.getCurrency())
                .setTimestamp(command.getTimestamp())
                .build();
        account.getTransactions().add(transaction);
    }
}
