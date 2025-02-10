package org.poo.bank.card;

import org.poo.bank.Constants;

public class CardFactory {
    /**
     * Creates a card based on the type
     * @param type the type of the card
     * @param cardNumber the card number
     * @param status the status of the card
     * @return the card
     */
    public Card createCard(final String type, final String cardNumber, final String status,
                           final String email) {
        return switch (type) {
            case Constants.CLASSIC_CARD -> new ClassicCard(cardNumber, status, email);
            case Constants.ONE_TIME_PAY_CARD -> new OneTimePayCard(cardNumber, status, email);
            default -> throw new IllegalArgumentException("The card type " + type
                    + " is not recognized.");
        };
    }
}
