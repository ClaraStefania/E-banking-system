package org.poo.bank;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.account.Account;
import org.poo.bank.account.BusinessAccount;
import org.poo.bank.card.Card;
import org.poo.fileio.CommandInput;

import java.util.List;

public final class CommandsUtils {
    private CommandsUtils() {
    }

    /**
     * gets the user after the email
     * @param users the list of users
     * @param email the email to be searched
     * @return the user with the email
     */
    public static User getUserAfterEmail(final List<User> users, final String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    /**
     * gets the card after the card number
     * @param account the account to be searched
     * @param cardNumber the card number to be searched
     * @return the card with the card number
     */
    public static Card getCardAfterNumber(final Account account, final String cardNumber) {
        for (Card card : account.getCards()) {
            if (card.getCardNumber().equals(cardNumber)) {
                return card;
            }
        }
        return null;
    }

    /**
     * gets the account after the iban
     * @param users the list of users
     * @param iban the iban to be searched
     * @return the account with the iban
     */
    public static Account getAccountAfterIban(final List<User> users, final String iban) {
        for (User user : users) {
            Account account = verifyIfAccountExists(user.getAccounts(), iban);
            if (account != null) {
                return account;
            }
        }
        return null;
    }

    /**
     * gets the user after the account's iban
     * @param users the list of users
     * @param iban the iban to be searched
     * @return the user
     */
    public static User getUserAfterIban(final List<User> users, final String iban) {
        for (User user : users) {
            Account account = verifyIfAccountExists(user.getAccounts(), iban);
            if (account != null) {
                return user;
            }
        }
        return null;
    }

    /**
     * gets the user after the card's number
     * @param users the list of users
     * @param cardNumber the card number to be searched
     * @return the user with the card number
     */
    public static User getUserAfterCardNumber(final List<User> users, final String cardNumber) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                Card card = verifyIfCardExists(account.getCards(), cardNumber);
                if (card != null) {
                    return user;
                }
            }
        }
        return null;
    }

    /**
     * gets the card after the card number
     * @param accounts the list of accounts
     * @param cardNumber the card number to be searched
     * @return the card with the card number
     */
    public static Card getCardAfterCardNumber(final List<Account> accounts,
                                              final String cardNumber) {
        for (Account account : accounts) {
            Card card = verifyIfCardExists(account.getCards(), cardNumber);
            if (card != null) {
                return card;
            }
        }
        return null;
    }

    /**
     * gets the commerciant after the account's iban
     * @param commerciants the list of commerciants
     * @param name the name to be searched
     * @return
     */
    public static Commerciant getCommerciantType(final List<Commerciant> commerciants,
                                                 final String name) {
        for (Commerciant commerciant : commerciants) {
            if (commerciant.getCommerciantName().equals(name)) {
                return commerciant;
            }
        }
        return null;
    }

    /**
     * gets the commerciant after the account's iban
     * @param commerciants the list of commerciants
     * @param iban the iban to be searched
     * @return the commerciant with the iban
     */
    public static Commerciant getCommerciantAfterAccount(final List<Commerciant> commerciants,
                                                         final String iban) {
        for (Commerciant commerciant : commerciants) {
            if (commerciant.getAccount().getIban().equals(iban)) {
                return commerciant;
            }
        }
        return null;
    }

    /**
     * gets the account after the card number
     * @param user the user to be searched
     * @param cardNumber the card number to be searched
     * @return the account with the card number
     */
    public static Account getAccountAfterCardNumber(final User user, final String cardNumber) {
        for (Account account : user.getAccounts()) {
            Card card = verifyIfCardExists(account.getCards(), cardNumber);
            if (card != null) {
                return account;
            }
        }
        return null;
    }

    /**
     * verifies if the account exists
     * @param accounts the list of accounts
     * @param iban the iban to be searched
     * @return the account with the iban
     */
    public static Account verifyIfAccountExists(final List<Account> accounts, final String iban) {
        for (Account account : accounts) {
            if (account.getIban().equals(iban)) {
                return account;
            }
        }
        return null;
    }

    /**
     * verifies if the card exists
     * @param cards the list of cards
     * @param cardNumber the card number to be searched
     * @return the card with the card number
     */
    public static Card verifyIfCardExists(final List<Card> cards, final String cardNumber) {
        for (Card card : cards) {
            if (card.getCardNumber().equals(cardNumber)) {
                return card;
            }
        }
        return null;
    }

    /**
     * verifies if the commerciant exists
     * @param commerciants the list of comerciants
     * @param name the name to be searched
     * @return the commerciant with the name
     */
    public static Commerciant verifyIfCommerciantExists(final List<Commerciant> commerciants,
                                                        final String name) {
        for (Commerciant commerciant : commerciants) {
            if (commerciant.getCommerciantName().equals(name)) {
                return commerciant;
            }
        }
        return null;
    }

    /**
     * verifies if a classic account exists
     * @param accounts the list of accounts
     * @return the classic account
     */
    public static Account verifyIfClassicAccountExists(final List<Account> accounts,
                                                       final String currency) {
        for (Account account : accounts) {
            if (account.getType().equals(Constants.CLASSIC_ACCOUNT)
                    && account.getCurrency().equals(currency)) {
                return account;
            }
        }
        return null;
    }

    /**
     * verifies if is an user account
     * @param users the list of users
     * @param iban the iban to be searched
     */
    public static Account verifyIfIsAnUserAccount(final List<User> users, final String iban) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(iban)) {
                    return account;
                }
            }
        }
        return null;
    }

    /**
     * verifies if is a commerciant account
     * @param commerciants the list of commerciants
     * @param iban the iban to be searched
     * @return the account with the iban
     */
    public static Account verifyIfIsACommerciantAccount(final List<Commerciant> commerciants,
                                                        final String iban) {
        for (Commerciant commerciant : commerciants) {
            if (commerciant.getAccount().getIban().equals(iban)) {
                return commerciant.getAccount();
            }
        }
        return null;
    }

    /**
     * verifies if the user is a manager
     * @param managers the list of managers
     * @param email the email to be searched
     * @return 1 if the user is a manager, 0 otherwise
     */
    public static Manager verifyIfUserIsManager(final List<Manager> managers, final String email) {
        for (Manager manager : managers) {
            if (manager.getUser().getEmail().equals(email)) {
                return manager;
            }
        }
        return null;
    }

    /**
     * verifies if the user is an employee
     * @param employees the list of employees
     * @param email the email to be searched
     * @return 1 if the user is an employee, 0 otherwise
     */
    public static Employee verifyIfUserIsEmployee(final List<Employee> employees,
                                                  final String email) {
        for (Employee employee : employees) {
            if (employee.getUser().getEmail().equals(email)) {
                return employee;
            }
        }
        return null;
    }

    /**
     * calculates the age of the user
     * @param birthDate the birthdate of the user
     * @return the age of the user
     */
    public static int calculateAge(final String birthDate) {
        String[] date = birthDate.split("-");
        int year = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]);
        int day = Integer.parseInt(date[2]);
        return Constants.YEAR - year - (month > Constants.MONTHS
                || (month == Constants.MONTHS && day > 1) ? 0 : 1);
    }

    /**
     * computes the error output
     * @param command the command input
     * @return the error output
     */
    public static ObjectNode computeErrorOutput(CommandInput command) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode errorNode = objectMapper.createObjectNode();
        errorNode.put("timestamp", command.getTimestamp());
        errorNode.put("description", Constants.CARD_NOT_FOUND);
        return errorNode;
    }

    /**
     * verifies if the user is an associate
     * @param account the business account
     * @param user the user to be verified
     * @return 1 if the user is an associate, 0 otherwise
     */
    public static int userIsAssociate(final BusinessAccount account, final User user) {
        if (CommandsUtils.verifyIfUserIsManager(account.getManagers(), user.getEmail()) != null) {
            return 1;
        }

        if (user.getEmail().equals(account.getOwner().getEmail())) {
            return 1;
        }

        if (CommandsUtils.verifyIfUserIsEmployee(account.getEmployees(),
                user.getEmail()) != null) {
            return 1;
        }

        return 0;
    }
}
