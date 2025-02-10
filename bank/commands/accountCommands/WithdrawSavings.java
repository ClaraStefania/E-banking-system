package org.poo.bank.commands.accountCommands;

import org.poo.bank.User;
import org.poo.bank.Command;
import org.poo.bank.CommandsUtils;
import org.poo.bank.Transaction;
import org.poo.bank.account.Account;
import org.poo.bank.account.SavingsAccount;
import org.poo.fileio.CommandInput;

import java.util.List;

public class WithdrawSavings implements Command {
    private final CommandInput command;
    private final List<User> users;

    public WithdrawSavings(final CommandInput command, final List<User> users) {
        this.command = command;
        this.users = users;
    }

    /**
     * withdraws funds from the account
     */
    @Override
    public void execute() {
        String iban = command.getAccount();
        Account account = CommandsUtils.getAccountAfterIban(users, iban);

        if (account == null) {
            return;
        }

        if (!account.getType().equals("savings")) {
            return;
        }

        User user = CommandsUtils.getUserAfterIban(users, iban);
        if (user == null) {
            return;
        }

        Account classicAccount = CommandsUtils.verifyIfClassicAccountExists(user.getAccounts(),
                command.getCurrency());
        if (classicAccount == null) {
            Transaction transaction = new Transaction.Builder()
                    .setDescription("You do not have a classic account.")
                    .setTimestamp(command.getTimestamp())
                    .build();
            account.getTransactions().add(transaction);
            return;
        }

        double initialBalance = account.getBalance();

        ((SavingsAccount) account).withdrawSavings(command.getAmount(), command.getTimestamp());
        if (account.getBalance() == initialBalance) {
            return;
        }

        classicAccount.acceptPayment(command.getAmount(), account.getCurrency());
        Transaction transaction = new Transaction.Builder()
                .setDescription("Savings withdrawal")
                .setSavingsAccountIban(account.getIban())
                .setClassicAccountIban(classicAccount.getIban())
                .setCardPayment(command.getAmount())
                .setTimestamp(command.getTimestamp())
                .build();
        account.getTransactions().add(transaction);
        classicAccount.getTransactions().add(transaction);
    }
}
