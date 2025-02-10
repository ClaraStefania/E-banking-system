package org.poo.bank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.account.Account;
import org.poo.bank.servicePlan.ServicePlan;
import org.poo.bank.servicePlan.ServicePlanFactory;

import java.util.LinkedList;
import java.util.List;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private List<Account> accounts;
    private String birthDate;
    private String occupation;
    private int age;
    private ServicePlan servicePlan;
    private ServicePlanFactory servicePlanFactory;
    private int nrOfSilverPayments;

    public User(final String firstName, final String lastName, final String email,
                final String birthDate, final String occupation) {
        this.accounts = new LinkedList<>();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthDate = birthDate;
        this.occupation = occupation;
        this.age = CommandsUtils.calculateAge(birthDate);
        this.servicePlanFactory = new ServicePlanFactory();
        if (occupation.equals(Constants.STUDENT)) {
            this.servicePlan = servicePlanFactory.createServiceplan(Constants.STUDENT);
        } else {
            this.servicePlan = servicePlanFactory.createServiceplan(Constants.STANDARD);
        }
        this.nrOfSilverPayments = 0;
    }

    /**
     * gets the user's first name
     * @return the value of firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * gets the user's last name
     * @return the value of lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * gets the user's email
     * @return the value of email
     */
    public String getEmail() {
        return email;
    }

    /**
     * gets the user's accounts
     * @return the value of accounts
     */
    public List<Account> getAccounts() {
        return accounts;
    }

    /**
     * gets the user's birthdate
     * @return the value of birthDate
     */
    public String getBirthDate() {
        return birthDate;
    }

    /**
     * gets the user's occupation
     * @return the value of occupation
     */
    public String getOccupation() {
        return occupation;
    }

    /**
     * gets the user's age
     * @return the value of age
     */
    public int getAge() {
        return age;
    }

    /**
     * gets the user's service plan
     * @return the value of servicePlan
     */
    public ServicePlan getServicePlan() {
        return servicePlan;
    }

    /**
     * gets the user's service plan factory
     * @return the value of servicePlanFactory
     */
    public ServicePlanFactory getServicePlanFactory() {
        return servicePlanFactory;
    }

    /**
     * gets the user's number of silver payments
     * @return the value of nrOfSilverPayments
     */
    public int getNrOfSilverPayments() {
        return nrOfSilverPayments;
    }

    /**
     * sets the user's first name
     * @param firstName the new value of firstName
     */
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    /**
     * sets the user's last name
     * @param lastName the new value of lastName
     */
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    /**
     * sets the user's email
     * @param email the new value of email
     */
    public void setEmail(final String email) {
        this.email = email;
    }

    /**
     * sets the user's accounts
     * @param accounts the new value of accounts
     */
    public void setAccounts(final List<Account> accounts) {
        this.accounts = accounts;
    }

    /**
     * sets the user's birthdate
     * @param birthDate the new value of birthDate
     */
    public void setBirthDate(final String birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * sets the user's occupation
     * @param occupation the new value of occupation
     */
    public void setOccupation(final String occupation) {
        this.occupation = occupation;
    }

    /**
     * sets the user's age
     * @param age the new value of age
     */
    public void setAge(final int age) {
        this.age = age;
    }

    /**
     * sets the user's service plan
     * @param servicePlan the new value of servicePlan
     */
    public void setServicePlan(final ServicePlan servicePlan) {
        this.servicePlan = servicePlan;
    }

    /**
     * sets the user's service plan factory
     * @param servicePlanFactory the new value of servicePlanFactory
     */
    public void setServicePlanFactory(final ServicePlanFactory servicePlanFactory) {
        this.servicePlanFactory = servicePlanFactory;
    }

    /**
     * sets the user's number of silver payments
     * @param nrOfSilverPayments the new value of nrOfSilverPayments
     */
    public void setNrOfSilverPayments(final int nrOfSilverPayments) {
        this.nrOfSilverPayments = nrOfSilverPayments;
    }

    /**
     * adds an account to the user's accounts
     */
    public ObjectNode computeOutput() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("firstName", this.firstName);
        node.put("lastName", this.lastName);
        node.put("email", this.email);
        return node;
    }
}
