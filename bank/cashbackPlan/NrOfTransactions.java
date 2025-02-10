package org.poo.bank.cashbackPlan;

import org.poo.bank.Constants;
import java.util.Map;

public class NrOfTransactions {
    private int nrOfTransactions;
    private int standardNrOfTransactions;
    private int receivedCashback;
    private String type;

    public NrOfTransactions(final int nrOfTransactions, final int receivedCashback,
                            final int standardNrOfTransactions, final String type) {
        this.nrOfTransactions = nrOfTransactions;
        this.standardNrOfTransactions = standardNrOfTransactions;
        this.receivedCashback = receivedCashback;
        this.type = type;
    }

    /**
     * gets the number of transactions
     * @return the value of nrOfTransactions
     */
    public int getNrOfTransactions() {
        return nrOfTransactions;
    }

    /**
     * gets the standard number of transactions
     * @return the value of standardNrOfTransactions
     */
    public int getStandardNrOfTransactions() {
        return standardNrOfTransactions;
    }

    /**
     * gets the type of the commerciant
     * @return the value of type
     */
    public String getType() {
        return type;
    }

    /**
     * sets the number of transactions
     * @param nrOfTransactions
     */
    public void setNrOfTransactions(final int nrOfTransactions) {
        this.nrOfTransactions = nrOfTransactions;
    }

    /**
     * sets the value of received cashback
     * @param receivedCashback the new received cashback
     */
    public void setReceivedCashback(final int receivedCashback) {
        this.receivedCashback = receivedCashback;
    }

    /**
     * sets the type of the commerciant
     * @param type the new type
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * calculates the cashback
     * @param amount the amount of the transaction
     * @param nrOfTransactionsMap the map with the number of transactions
     * @return the cashback
     */
    public double calculateCashback(final double amount,
                                    final Map<String, NrOfTransactions> nrOfTransactionsMap) {
        switch (type) {
            case "Food" -> receivedCashbackFood(nrOfTransactionsMap);
            case "Clothes" -> receivedCashbackClothes(nrOfTransactionsMap);
            case "Tech" -> receivedCashbackTech(nrOfTransactionsMap);
            default -> {
                System.out.println("Invalid type");
                return 0;
            }
        }

        return amount * Constants.CASHBACK_PERCENTAGE * standardNrOfTransactions;
    }

    /**
     * marks the food transactions that received cashback
     * @param nrOfTransactionsMap the map with the transactions
     */
    public void receivedCashbackFood(final Map<String, NrOfTransactions> nrOfTransactionsMap) {
        for (Map.Entry<String, NrOfTransactions> entry : nrOfTransactionsMap.entrySet()) {
            if (entry.getValue().getType().equals("Food")) {
                entry.getValue().setReceivedCashback(1);
            }
        }
    }

    /**
     * marks the clothes transactions that received cashback
     * @param nrOfTransactionsMap the map with the transactions
     */
    public void receivedCashbackClothes(final Map<String, NrOfTransactions> nrOfTransactionsMap) {
        for (Map.Entry<String, NrOfTransactions> entry : nrOfTransactionsMap.entrySet()) {
            if (entry.getValue().getType().equals("Clothes")) {
                entry.getValue().setReceivedCashback(1);
            }
        }
    }

    /**
     * marks the tech transactions that received cashback
     * @param nrOfTransactionsMap the map with the transactions
     */
    public void receivedCashbackTech(final Map<String, NrOfTransactions> nrOfTransactionsMap) {
        for (Map.Entry<String, NrOfTransactions> entry : nrOfTransactionsMap.entrySet()) {
            if (entry.getValue().getType().equals("Tech")) {
                entry.getValue().setReceivedCashback(1);
            }
        }
    }
}
