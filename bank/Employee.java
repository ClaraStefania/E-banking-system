package org.poo.bank;

import java.util.ArrayList;
import java.util.List;

public class Employee {
    private User user;
    private List<String> accounts;
    private List<String> cards;
    private double spent;
    private double deposited;

    public Employee(final User user) {
        this.user = user;
        this.accounts = new ArrayList<>();
        this.cards = new ArrayList<>();
        this.spent = 0;
        this.deposited = 0;
    }

    /**
     * gets the user of the employee
     * @return the value of user
     */
    public User getUser() {
        return user;
    }

    /**
     * gets the accounts of the employee
     * @return the value of accounts
     */
    public List<String> getAccounts() {
        return accounts;
    }

    /**
     * gets the cards of the employee
     * @return the value of cards
     */
    public List<String> getCards() {
        return cards;
    }

    /**
     * gets the amount spent
     * @return the value of the spent amount
     */
    public double getSpent() {
        return spent;
    }

    /**
     * gets the amount deposited
     * @return the value of the deposited amount
     */
    public double getDeposited() {
        return deposited;
    }

    /**
     * sets the user of the employee
     * @param user the new user
     */
    public void setUser(final User user) {
        this.user = user;
    }

    /**
     * sets the cards of the employee
     * @param cards the new cards
     */
    public void setCards(final List<String> cards) {
        this.cards = cards;
    }

    /**
     * sets the accounts of the employee
     * @param accounts the new accounts
     */
    public void setAccounts(final List<String> accounts) {
        this.accounts = accounts;
    }

    /**
     * sets the spent amount
     * @param spent the new spent amount
     */
    public void setSpent(final double spent) {
        this.spent = spent;
    }

    /**
     * sets the deposited amount
     * @param deposited the new deposited amount
     */
    public void setDeposited(final double deposited) {
        this.deposited = deposited;
    }
}
