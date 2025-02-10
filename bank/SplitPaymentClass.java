package org.poo.bank;

import org.poo.bank.account.Account;

import java.util.ArrayList;
import java.util.List;

public class SplitPaymentClass {
    private List<Double> amountsForUsers;
    private List<Account> validAccounts;
    private List<Integer> acceptedPayments;
    private String currency;
    private List<User> implicatedUsers;
    private int timestamp;
    private List<String> ibans;
    private String type;
    private double amount;

    public SplitPaymentClass(final List<Double> amountsForUsers, final List<Account> validAccounts,
                             final String currency, final List<User> implicatedUsers,
                             final int timestamp, final List<String> ibans, final String type,
                             final double amount) {
        this.amountsForUsers = amountsForUsers;
        this.validAccounts = validAccounts;
        this.acceptedPayments = new ArrayList<>();
        for (int i = 0; i < implicatedUsers.size(); i++) {
            acceptedPayments.add(0);
        }
        this.currency = currency;
        this.implicatedUsers = implicatedUsers;
        this.timestamp = timestamp;
        this.ibans = ibans;
        this.type = type;
        this.amount = amount;
    }

    /**
     * gets the amounts for users
     * @return the value of amountsForUsers
     */
    public List<Double> getAmountsForUsers() {
        return amountsForUsers;
    }

    /**
     * gets the valid accounts
     * @return the value of validAccounts
     */
    public List<Account> getValidAccounts() {
        return validAccounts;
    }

    /**
     * gets the accepted payments
     * @return the value of acceptedPayments
     */
    public List<Integer> getAcceptedPayments() {
        return acceptedPayments;
    }

    /**
     * gets the currency
     * @return the value of currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * gets the implicated users
     * @return the value of implicatedUsers
     */
    public List<User> getImplicatedUsers() {
        return implicatedUsers;
    }

    /**
     * gets the timestamp of the payment
     * @return the value of timestamp
     */
    public int getTimestamp() {
        return timestamp;
    }

    /**
     * gets the ibans
     * @return the value of ibans
     */
    public List<String> getIbans() {
        return ibans;
    }

    /**
     * gets the type
     * @return the value of type
     */
    public String getType() {
        return type;
    }

    /**
     * gets the amount
     * @return the value of amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * sets the currency
     * @param currency the new currency
     */
    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    /**
     * sets the timestamp
     * @param timestamp the new timestamp
     */
    public void setTimestamp(final int timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * sets the type
     * @param type the new type
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * sets the amount
     * @param amount the new amount
     */
    public void setAmount(final double amount) {
        this.amount = amount;
    }

    /**
     * verifies if all users responded
     * @return 1 if all users responded, 0 otherwise
     */
    public int verifyIfAllUsersResponded() {
        for (Integer acceptedPayment : acceptedPayments) {
            if (acceptedPayment == 0) {
                return 0;
            }
        }
        return 1;
    }

    /**
     * verifies if someone rejected the payment
     * @return 1 if someone rejected the payment, 0 otherwise
     */
    public int verifyIfSomeoneRejected() {
        for (Integer acceptedPayment : acceptedPayments) {
            if (acceptedPayment == 2) {
                return 1;
            }
        }
        return 0;
    }

    /**
     * verifies if the user accepted the payment
     * @param user the user to be verified
     * @return 1 if the user accepted the payment, 0 otherwise
     */
    public int verifyIfUserAccepted(final User user) {
        for (int i = 0; i < implicatedUsers.size(); i++) {
            if (implicatedUsers.get(i).equals(user)) {
                return acceptedPayments.get(i);
            }
        }
        return 0;
    }

    /**
     * pay the amount for each account
     */
    public void payAllAccounts() {
        for (int i = 0; i < validAccounts.size(); i++) {
            validAccounts.get(i).payOnline(amountsForUsers.get(i), currency);
        }
    }
}
