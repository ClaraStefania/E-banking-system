package org.poo.bank.commands.accountCommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Command;
import org.poo.bank.Constants;
import org.poo.bank.User;
import org.poo.bank.CommandsUtils;
import org.poo.bank.Transaction;
import org.poo.bank.account.Account;
import org.poo.bank.account.SavingsAccount;
import org.poo.fileio.CommandInput;

import java.util.List;

public class ChangeInterestRate implements Command {
    private final CommandInput command;
    private final List<User> users;
    private final ArrayNode output;
    private final ObjectMapper objectMapper;

    public ChangeInterestRate(final CommandInput command, final List<User> users,
                              final ArrayNode output) {
        this.command = command;
        this.users = users;
        this.output = output;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * changes the interest rate of the account and computes the transaction
     */
    @Override
    public void execute() {
        double interestRate = command.getInterestRate();
        String iban = command.getAccount();
        Account account = CommandsUtils.getAccountAfterIban(users, iban);
        if (account == null) {
            return;
        }

        if (account.getType().equals(Constants.CLASSIC_ACCOUNT)) {
            ObjectNode rootNode =  account.computeErrorOutput(objectMapper, command);
            output.add(rootNode);
            return;
        }

        ((SavingsAccount) account).setInterestRate(interestRate);
        Transaction transaction = new Transaction.Builder()
                .setDescription("Interest rate of the account changed to " + interestRate)
                .setTimestamp(command.getTimestamp())
                .build();
        account.getTransactions().add(transaction);
    }
}
