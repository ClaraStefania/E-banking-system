package org.poo.bank.card;

import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class Card {
    protected String cardNumber;
    protected String status;
    protected String type;
    protected String email;

    public Card(final String cardNumber, final String status, final String email) {
        this.cardNumber = cardNumber;
        this.status = status;
        this.email = email;
    }

    /**
     * gets the cards' number
     * @return the value of card number
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * gets the cards' status
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * gets the cards' type
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * gets the email of the card;s user
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * sets the status of the card
     * @param status the new status
     */
    public void setStatus(final String status) {
        this.status = status;
    }

    /**
     * sets the card number
     * @param cardNumber the new card number
     */
    public void setCardNumber(final String cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * sets the type of the card
     * @param type the new type
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * sets the email of the card's user
     * @param email the new email
     */
    public void setEmail(final String email) {
        this.email = email;
    }

    /**
     * computes the output
     * @return the output
     */
    public abstract ObjectNode computeOutput();

}
