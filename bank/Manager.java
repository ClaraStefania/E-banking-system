package org.poo.bank;

public class Manager {
    private User user;
    private double spent;
    private double deposited;

    public Manager(final User user) {
        this.user = user;
        this.spent = 0;
        this.deposited = 0;
    }

    /**
     * gets the user of the manager
     * @return the value of user
     */
    public User getUser() {
        return user;
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
     * sets the user of the manager
     * @param user the new user
     */
    public void setUser(final User user) {
        this.user = user;
    }

    /**
     * sets the amount spent
     * @param spent the new spent amount
     */
    public void setSpent(final double spent) {
        this.spent = spent;
    }

    /**
     * sets the amount deposited
     * @param deposited the new deposited amount
     */
    public void setDeposited(final double deposited) {
        this.deposited = deposited;
    }
}
