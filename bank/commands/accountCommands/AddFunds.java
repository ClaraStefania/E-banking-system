package org.poo.bank.commands.accountCommands;

import org.poo.bank.Command;
import org.poo.bank.CommandsUtils;
import org.poo.bank.Constants;
import org.poo.bank.User;
import org.poo.bank.Manager;
import org.poo.bank.Employee;
import org.poo.bank.account.Account;
import org.poo.bank.account.BusinessAccount;
import org.poo.fileio.CommandInput;

import java.util.List;

public class AddFunds implements Command {
    private final CommandInput command;
    private final List<User> users;

    public AddFunds(final CommandInput command, final List<User> users) {
        this.command = command;
        this.users = users;
    }

    /**
     * adds funds to the account
     */
    @Override
    public void execute() {
        String iban = command.getAccount();
        Account account = CommandsUtils.getAccountAfterIban(users, iban);
        String email = command.getEmail();

        if (account == null) {
            return;
        }

        User user = CommandsUtils.getUserAfterEmail(users, email);
        if (user == null) {
            return;
        }

        if (account.getType().equals(Constants.BUSINESS_ACCOUNT)) {
            Manager manager = CommandsUtils.verifyIfUserIsManager(
                    ((BusinessAccount) account).getManagers(), user.getEmail());
            Employee employee = CommandsUtils.verifyIfUserIsEmployee(
                    ((BusinessAccount) account).getEmployees(), user.getEmail());

            // verify if the user is an associate
            if (!email.equals(((BusinessAccount) account).getOwner().getEmail())
                    && manager == null && employee == null) {
                return;
            }

            double depositLimit = ((BusinessAccount) account).getDepositLimit();
            if (employee != null && command.getAmount() > depositLimit) {
                    return;
            }
            account.addFunds(command.getAmount(), email);
        } else {
            account.addFunds(command.getAmount(), null);
        }
    }
}
