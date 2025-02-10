package org.poo.bank.commands.cardCommands;

import org.poo.bank.Command;
import org.poo.bank.Constants;
import org.poo.bank.CommandsUtils;
import org.poo.bank.Transaction;
import org.poo.bank.User;
import org.poo.bank.account.Account;
import org.poo.bank.account.BusinessAccount;
import org.poo.bank.card.Card;
import org.poo.bank.card.CardFactory;
import org.poo.fileio.CommandInput;
import org.poo.utils.Utils;

import java.util.List;

public class CreateCard implements Command {
    private final CommandInput command;
    private final List<User> users;
    private final String cardType;

    public CreateCard(final CommandInput command, final List<User> users, final String cardType) {
        this.command = command;
        this.users = users;
        this.cardType = cardType;
    }

    /**
     * creates a new card for the account and adds a transaction
     */
    @Override
    public void execute() {
        String email = command.getEmail();
        String iban = command.getAccount();

        // verify if the user and account exist
        User user = CommandsUtils.getUserAfterEmail(users, email);
        if (user == null) {
            return;
        }

        Account account = CommandsUtils.verifyIfAccountExists(user.getAccounts(), iban);
        if (account == null) {
            return;
        }

        String cardNumber = Utils.generateCardNumber();
        CardFactory cardFactory = new CardFactory();
        Card newCard;
        if (account.getType().equals(Constants.BUSINESS_ACCOUNT)) {
            if (CommandsUtils.userIsAssociate((BusinessAccount) account, user) == 0) {
                return;
            }
            newCard = cardFactory.createCard(cardType, cardNumber, Constants.ACTIVE, email);
        } else {
            newCard = cardFactory.createCard(cardType, cardNumber, Constants.ACTIVE, null);
            Transaction transaction = new Transaction.Builder()
                    .setDescription("New card created")
                    .setTimestamp(command.getTimestamp())
                    .setCardNumber(cardNumber)
                    .setCardHolder(user.getEmail())
                    .setAccount(account.getIban())
                    .build();

            account.getTransactions().add(transaction);
        }
        account.getCards().add(newCard);
    }
}
