package org.poo.bank.commands.accountCommands;

import org.poo.bank.Constants;
import org.poo.bank.User;
import org.poo.bank.Command;
import org.poo.bank.CommandsUtils;
import org.poo.bank.SplitPaymentClass;

import org.poo.bank.account.Account;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;
import java.util.List;

public class SplitPayment implements Command {
    private final CommandInput command;
    private final List<User> users;
    private final List<Account> validAccounts;
    private final List<SplitPaymentClass> splitPayments;
    private final List<User> implicatedUsers;

    public SplitPayment(final CommandInput command, final List<User> users,
                        final List<SplitPaymentClass> splitPayments) {
        this.command = command;
        this.users = users;
        this.validAccounts = new ArrayList<>();
        this.splitPayments = splitPayments;
        this.implicatedUsers = new ArrayList<>();
    }

    /**
     * splits the payment between the accounts
     */
    @Override
    public void execute() {
        double amount = command.getAmount();
        List<Double> amountsToPay;
        String splitType = command.getSplitPaymentType();
        List<String> ibans = command.getAccounts();
        if (splitType.equals("equal")) {
            amountsToPay = new ArrayList<>();
            for (int i = 0; i < ibans.size(); i++) {
                amountsToPay.add(amount / ibans.size());
            }
        } else {
            amountsToPay = command.getAmountForUsers();
        }

        // checks if the accounts are valid
        for (String iban : ibans) {
            Account account = CommandsUtils.getAccountAfterIban(users, iban);
            if (account == null) {
                return;
            }

            if (account.getType().equals(Constants.BUSINESS_ACCOUNT)) {
                return;
            }

            validAccounts.add(account);
            implicatedUsers.add(CommandsUtils.getUserAfterIban(users, iban));
        }

        // adds the split payment to the list
        SplitPaymentClass splitPaymentClass = new SplitPaymentClass(amountsToPay, validAccounts,
                command.getCurrency(), implicatedUsers, command.getTimestamp(), ibans, splitType,
                command.getAmount());
        splitPayments.add(splitPaymentClass);
    }
}
