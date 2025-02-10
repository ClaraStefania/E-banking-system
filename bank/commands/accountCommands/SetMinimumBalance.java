package org.poo.bank.commands.accountCommands;

import org.poo.bank.Command;
import org.poo.bank.CommandsUtils;
import org.poo.bank.Constants;
import org.poo.bank.User;
import org.poo.bank.account.Account;
import org.poo.bank.account.BusinessAccount;
import org.poo.fileio.CommandInput;

import java.util.List;

public class SetMinimumBalance implements Command {
    private final CommandInput command;
    private final List<User> users;

    public SetMinimumBalance(final CommandInput command, final List<User> users) {
        this.command = command;
        this.users = users;
    }

    /**
     * sets the minimum balance for the account
     */
    @Override
    public void execute() {
        String iban = command.getAccount();
        for (User user : users) {
            Account account = CommandsUtils.verifyIfAccountExists(user.getAccounts(), iban);
            if (account != null && account.getType().equals(Constants.BUSINESS_ACCOUNT)
                    && !user.getEmail().equals(((BusinessAccount) account).getOwner().getEmail())) {
                return;
            } else if (account != null) {
                account.setMinBalance(command.getAmount());
                return;
            }
        }
    }
}
