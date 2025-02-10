package org.poo.bank;

import org.poo.bank.account.Account;

import java.util.ArrayList;
import java.util.List;

public class Commerciant {
    private final String commerciantName;
    private double amount;
    private int id;
    private Account account;
    private String type;
    private String cashbackStrategy;
    private List<Manager> managers;
    private List<Employee> employees;

    public Commerciant(final String commerciantName, final double amount, final int id,
                       final Account account, final String type, final String cashbackStrategy) {
        this.commerciantName = commerciantName;
        this.amount = amount;
        this.id = id;
        this.account = account;
        this.type = type;
        this.cashbackStrategy = cashbackStrategy;
        this.managers = new ArrayList<>();
        this.employees = new ArrayList<>();
    }

    /**
     * gets the id of the commerciant
     * @return the value of id
     */
    public int getId() {
        return id;
    }

    /**
     * gets the account of the commerciant
     * @return the value of account
     */
    public Account getAccount() {
        return account;
    }

    /**
     * gets the type of the commerciant
     * @return the value of type
     */
    public String getType() {
        return type;
    }

    /**
     * gets the cashback strategy of the commerciant
     * @return the value of cashbackStrategy
     */
    public String getCashbackStrategy() {
        return cashbackStrategy;
    }

    /**
     * gets the name of the account
     * @return the value of commerciant
     */
    public String getCommerciantName() {
        return commerciantName;
    }

    /**
     * gets the amount of the account
     * @return the value of amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * gets the managers list
     * @return the list of managers
     */
    public List<Manager> getManagers() {
        return managers;
    }

    /**
     * gets the employees list
     * @return the list of employees
     */
    public List<Employee> getEmployees() {
        return employees;
    }

    /**
     * sets the amount of the commerciant
     * @param amount the new value of amount
     */
    public void setAmount(final double amount) {
        this.amount = amount;
    }

    /**
     * sets the id of the commerciant
     * @param id the new value of id
     */
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * sets the account of the commerciant
     * @param account the new value of account
     */
    public void setAccount(final Account account) {
        this.account = account;
    }

    /**
     * sets the type of the commerciant
     * @param type the new value of type
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * sets the cashback strategy of the commerciant
     * @param cashbackStrategy the new value of cashbackStrategy
     */
    public void setCashbackStrategy(final String cashbackStrategy) {
        this.cashbackStrategy = cashbackStrategy;
    }

    /**
     * sets the managers list
     * @param managers the new list of managers
     */
    public void setManagers(final List<Manager> managers) {
        this.managers = managers;
    }

    /**
     * sets the employees list
     * @param employees the new list of employees
     */
    public void setEmployees(final List<Employee> employees) {
        this.employees = employees;
    }

    /**
     * adds a commerciant alphabetically in the list
     */
    public void addCommerciantAlphabetically(final List<Commerciant> commerciants,
                                             final Commerciant commerciant) {
        int i = 0;
        while (i < commerciants.size()
               && commerciants.get(i).getCommerciantName().compareTo(
                       commerciant.getCommerciantName()) < 0) {
            i++;
        }
        commerciants.add(i, commerciant);
    }
}
