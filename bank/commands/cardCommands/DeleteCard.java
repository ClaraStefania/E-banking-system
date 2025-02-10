package org.poo.bank.commands.cardCommands;

import org.poo.bank.Command;
import org.poo.bank.Constants;
import org.poo.bank.CommandsUtils;
import org.poo.bank.Transaction;
import org.poo.bank.User;
import org.poo.bank.account.Account;
import org.poo.bank.account.BusinessAccount;
import org.poo.bank.card.Card;
import org.poo.fileio.CommandInput;

import java.util.List;

public class DeleteCard implements Command {
    private final CommandInput command;
    private final List<User> users;

    public DeleteCard(final CommandInput command, final List<User> users) {
        this.command = command;
        this.users = users;
    }

    /**
     * deletes a card from the account and adds a transaction
     */
    @Override
    public void execute() {
        String email = command.getEmail();
        String cardNumber = command.getCardNumber();
        User user = CommandsUtils.getUserAfterEmail(users, email);
        if (user == null) {
            return;
        }

        for (Account account : user.getAccounts()) {
            Card card = CommandsUtils.verifyIfCardExists(account.getCards(), cardNumber);
            if (card == null) {
                continue;
            }

            if (account.getType().equals(Constants.BUSINESS_ACCOUNT)
                && CommandsUtils.userIsAssociate((BusinessAccount) account, user) == 0) {
                return;
            }

            if (account.getBalance() > 0) {
                return;
            }

            account.getCards().remove(card);
            Transaction transaction = new Transaction.Builder()
                    .setDescription(Constants.CARD_DESTROYED)
                    .setTimestamp(command.getTimestamp())
                    .setCardNumber(cardNumber)
                    .setCardHolder(user.getEmail())
                    .setAccount(account.getIban())
                    .build();

            account.getTransactions().add(transaction);
            return;
        }
    }
}
