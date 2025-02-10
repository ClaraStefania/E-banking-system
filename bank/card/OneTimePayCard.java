package org.poo.bank.card;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Constants;

public class OneTimePayCard extends Card {
    public OneTimePayCard(final String cardNumber, final String status, final String email) {
        super(cardNumber, status, email);
        this.type = Constants.ONE_TIME_PAY_CARD;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectNode computeOutput() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode cardNode = objectMapper.createObjectNode();
        cardNode.put("cardNumber", cardNumber);
        cardNode.put("status", status);
        return cardNode;
    }
}
