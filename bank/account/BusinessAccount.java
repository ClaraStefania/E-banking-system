package org.poo.bank.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.CommandsUtils;
import org.poo.bank.Constants;
import org.poo.bank.User;
import org.poo.bank.Manager;
import org.poo.bank.Employee;
import org.poo.bank.Commerciant;
import org.poo.bank.cashbackPlan.NrOfTransactions;
import org.poo.bank.exchange.CurrencyGraph;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BusinessAccount extends Account {
    private User owner;
    private List<Manager> managers;
    private List<Employee> employees;
    private double paymentLimit;
    private double depositLimit;
    private double initialLimit;
    private List<Commerciant> commerciants;

    public BusinessAccount(final String iban, final String currency, final String type,
                          final CurrencyGraph currencyGraph, final int usersAge, final User owner,
                           final Map<String, NrOfTransactions> spendingThresholdMap) {
        super(iban, currency, type, currencyGraph, usersAge, spendingThresholdMap);
        this.owner = owner;
        this.managers = new LinkedList<>();
        this.employees = new LinkedList<>();
        double exchangeRate = currencyGraph.calculateExchangeRate(Constants.RON, currency);

        initialLimit = Constants.INITIAL_LIMIT * exchangeRate;
        this.paymentLimit = initialLimit;
        this.depositLimit = initialLimit;
        this.commerciants = new ArrayList<>();
    }

    /**
     * gets the managers
     * @return the list of managers
     */
    public List<Manager> getManagers() {
        return managers;
    }

    /**
     * gets the employees
     * @return the list of employees
     */
    public List<Employee> getEmployees() {
        return employees;
    }

    /**
     * gets the payment limit
     * @return the payment limit
     */
    public double getPaymentLimit() {
        return paymentLimit;
    }

    /**
     * gets the owner
     * @return the owner
     */
    public User getOwner() {
        return owner;
    }

    /**
     * gets the deposit limit
     * @return the deposit limit
     */
    public double getDepositLimit() {
        return depositLimit;
    }

    /**
     * gets the commerciants
     * @return the list of commerciants
     */
    public List<Commerciant> getCommerciants() {
        return commerciants;
    }

    /**
     * sets the managers
     * @param managers the list of managers
     */
    public void setManagers(final List<Manager> managers) {
        this.managers = managers;
    }

    /**
     * sets the employees
     * @param employees the list of employees
     */
    public void setEmployees(final List<Employee> employees) {
        this.employees = employees;
    }

    /**
     * sets the payment limit
     * @param paymentLimit the new payment limit
     */
    public void setPaymentLimit(final double paymentLimit) {
        this.paymentLimit = paymentLimit;
    }

    /**
     * sets the owner
     * @param owner the new owner
     */
    public void setOwner(final User owner) {
        this.owner = owner;
    }

    /**
     * sets the deposit limit
     * @param depositLimit the new deposit limit
     */
    public void setDepositLimit(final double depositLimit) {
        this.depositLimit = depositLimit;
    }

    /**
     * sets the commerciants
     * @param commerciants the list of commerciants
     */
    public void setCommerciants(final List<Commerciant> commerciants) {
        this.commerciants = commerciants;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectNode computeErrorOutput(final ObjectMapper objectMapper,
                                         final CommandInput command) {
        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("command", command.getCommand());
        ObjectNode reportNode = objectMapper.createObjectNode();
        reportNode.put("timestamp", command.getTimestamp());
        reportNode.put("description", Constants.THIS_IS_NOT_A_SAVINGS_ACCOUNT);
        rootNode.set("output", reportNode);
        rootNode.put("timestamp", command.getTimestamp());
        return rootNode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addFunds(final double amount, final String userEmail) {
        Manager manager = CommandsUtils.verifyIfUserIsManager(managers, userEmail);

        if (userEmail.equals(owner.getEmail())) {
            balance += amount;
        } else if (manager != null) {
            balance += amount;
            manager.setDeposited(manager.getDeposited() + amount);
        } else {
            Employee employee = CommandsUtils.verifyIfUserIsEmployee(employees, userEmail);
            if (employee != null && amount <= depositLimit) {
                balance += amount;
                employee.setDeposited(employee.getDeposited() + amount);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double acceptPayment(final double amount, final String paymentCurrency) {
        double exchangeRate = currencyGraph.calculateExchangeRate(paymentCurrency, currency);
        if (amount * exchangeRate <= paymentLimit) {
            balance += amount * exchangeRate;
            return amount * exchangeRate;
        }

        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int payOnline(final double amount, final String paymentCurrency) {
        if (amount == 0) {
            return 1;
        }

        double exchangeRate = currencyGraph.calculateExchangeRate(paymentCurrency, currency);
        double newBalance = balance - amount * exchangeRate;

        // verify if the payment can be made
        if (newBalance >= 0) {
            balance = newBalance;
            return 0;
        }

        return 1;
    }
}
