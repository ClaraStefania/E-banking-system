package org.poo.bank.commands.accountCommands;

import org.poo.bank.*;
import org.poo.bank.account.Account;
import org.poo.bank.account.BusinessAccount;
import org.poo.fileio.CommandInput;

import java.util.List;

public class AddNewBusinessAssociate implements Command {
    private final CommandInput command;
    private final List<User> users;

    public AddNewBusinessAssociate(final CommandInput command, final List<User> users) {
        this.command = command;
        this.users = users;
    }

    /**
     * adds a new associate to the business account
     */
    @Override
    public void execute() {
        String iban = command.getAccount();
        Account account = CommandsUtils.getAccountAfterIban(users, iban);
        String email = command.getEmail();
        String role = command.getRole();

        if (account == null) {
            return;
        }

        if (!account.getType().equals(Constants.BUSINESS_ACCOUNT)) {
            return;
        }

        User user = CommandsUtils.getUserAfterEmail(users, email);
        if (user == null) {
            return;
        }

        if (CommandsUtils.userIsAssociate((BusinessAccount) account, user) == 1) {
            return;
        }

        if (role.equals(Constants.MANAGER)) {
            Manager manager = new Manager(user);
            ((BusinessAccount) account).getManagers().add(manager);
            user.getAccounts().add(account);
        } else if (role.equals(Constants.EMPLOYEE)) {
            Employee employee = new Employee(user);
            ((BusinessAccount) account).getEmployees().add(employee);
            user.getAccounts().add(account);
        }
    }
}
