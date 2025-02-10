package org.poo.bank.commands.userCommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Command;
import org.poo.bank.Constants;
import org.poo.bank.User;
import org.poo.bank.account.Account;
import org.poo.bank.account.BusinessAccount;
import org.poo.bank.card.Card;
import org.poo.fileio.CommandInput;

import java.util.List;

public class PrintUsers implements Command {
    private final CommandInput command;
    private final List<User> users;
    private final ArrayNode output;
    private final ObjectMapper objectMapper;

    public PrintUsers(final CommandInput command, final List<User> users,
                      final ArrayNode output) {
        this.command = command;
        this.users = users;
        this.output = output;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * computes the output of the cards
     * @param account the account to print the cards from
     * @return the array of cards
     */
    public ArrayNode printCards(final Account account) {
        ArrayNode cardsArray = objectMapper.createArrayNode();
        for (Card card : account.getCards()) {
            ObjectNode cardNode = card.computeOutput();
            cardsArray.add(cardNode);
        }
        return cardsArray;
    }

    /**
     * computes the output of the accounts
     * @param user the user to print the accounts from
     * @return the array of accounts
     */
    public ArrayNode printAccounts(final User user) {
        ArrayNode accountsArray = objectMapper.createArrayNode();
        for (Account account : user.getAccounts()) {
            if (account.getType().equals(Constants.BUSINESS_ACCOUNT) &&
                    !((BusinessAccount) account).getOwner().getEmail().equals(user.getEmail())) {
                continue;
            }
            ObjectNode accountNode = account.computeOutput();
            ArrayNode cardsArray = printCards(account);
            accountNode.set("cards", cardsArray);

            accountsArray.add(accountNode);
        }
        return accountsArray;
    }

    /**
     * puts the users in the output
     */
    @Override
    public void execute() {
        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("command", Constants.PRINT_USERS);

        ArrayNode outputArray = objectMapper.createArrayNode();

        for (User user : users) {
            ObjectNode userNode = user.computeOutput();
            ArrayNode accountsArray = printAccounts(user);
            userNode.set("accounts", accountsArray);
            outputArray.add(userNode);
        }
        rootNode.set("output", outputArray);
        rootNode.put("timestamp", command.getTimestamp());

        output.add(rootNode);
    }
}
