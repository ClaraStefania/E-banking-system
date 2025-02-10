package org.poo.bank.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Transaction;
import org.poo.bank.card.Card;
import org.poo.bank.cashbackPlan.NrOfTransactions;
import org.poo.bank.cashbackPlan.SpendingThreshold;
import org.poo.bank.exchange.CurrencyGraph;
import org.poo.fileio.CommandInput;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class Account {
    protected String iban;
    protected double balance;
    protected String currency;
    protected String type;
    protected List<Card> cards;
    protected List<Transaction> transactions;
    protected double minBalance;
    protected CurrencyGraph currencyGraph;
    protected int usersAge;
    protected Map<String, NrOfTransactions> nrOfTransactionsMap;
    protected Map<String, SpendingThreshold> spendingThresholdMap;
    protected double amountSpendingThreshold;
    protected int cashbackToReceiveFood;
    protected int cashbackToReceiveClothes;
    protected int cashbackToReceiveTech;

    public Account(final String iban, final String currency, final String type,
                   final CurrencyGraph currencyGraph, final int usersAge,
                   final Map<String, NrOfTransactions> nrOfTransactionsMap) {
        this.cards = new LinkedList<>();
        this.iban = iban;
        this.balance = 0.0;
        this.currency = currency;
        this.type = type;
        this.transactions = new LinkedList<>();
        this.minBalance = 0;
        this.currencyGraph = currencyGraph;
        this.usersAge = usersAge;
        this.nrOfTransactionsMap = nrOfTransactionsMap;
        this.spendingThresholdMap = new HashMap<>();
        spendingThresholdMap.put("Food", new SpendingThreshold(0));
        spendingThresholdMap.put("Clothes", new SpendingThreshold(0));
        spendingThresholdMap.put("Tech", new SpendingThreshold(0));
        this.amountSpendingThreshold = 0;
        this.cashbackToReceiveFood = 0;
        this.cashbackToReceiveClothes = 0;
        this.cashbackToReceiveTech = 0;
    }

    /**
     * gets the account's IBAN
     * @return the value of IBAN
     */
    public String getIban() {
        return iban;
    }

    /**
     * gets the account's balance
     * @return the value of balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * gets the account's currency
     * @return the value of currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * gets the account's type
     * @return the value of type
     */
    public String getType() {
        return type;
    }

    /**
     * gets the account's cards
     * @return the value of cards
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * gets the account's transactions
     * @return the value of transactions
     */
    public List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * gets the account's minimum balance
     * @return the value of minBalance
     */
    public double getMinBalance() {
        return minBalance;
    }

    /**
     * gets the account's currency graph
     * @return the value of currencyGraph
     */
    public CurrencyGraph getCurrencyGraph() {
        return currencyGraph;
    }

    /**
     * gets the nrOfTransactions map
     * @return the value of nrOfTransactionsMap
     */
    public Map<String, NrOfTransactions> getNrOfTransactionsMap() {
        return nrOfTransactionsMap;
    }

    /**
     * gets the spendingThreshold map
     * @return the value of spendingThresholdMap
     */
    public Map<String, SpendingThreshold> getSpendingThresholdMap() {
        return spendingThresholdMap;
    }

    /**
     * gets the amount spending threshold
     * @return the value of amountSpendingTreshold
     */
    public double getAmountSpendingThreshold() {
        return amountSpendingThreshold;
    }

    /**
     * gets the cashback to receive for food
     * @return the value of cashbackToReceiveFood
     */
    public int getCashbackToReceiveFood() {
        return cashbackToReceiveFood;
    }

    /**
     * gets the cashback to receive for clothes
     * @return the value of cashbackToReceiveClothes
     */
    public int getCashbackToReceiveClothes() {
        return cashbackToReceiveClothes;
    }

    /**
     * gets the cashback to receive for tech
     * @return the value of cashbackToReceiveTech
     */
    public int getCashbackToReceiveTech() {
        return cashbackToReceiveTech;
    }

    /**
     * sets the IBAN of the account
     * @param iban the new IBAN
     */
    public void setIban(final String iban) {
        this.iban = iban;
    }

    /**
     * sets the balance of the account
     * @param balance the new balance
     */
    public void setBalance(final double balance) {
        this.balance = balance;
    }

    /**
     * sets the currency of the account
     * @param currency the new currency
     */
    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    /**
     * sets the type of the account
     * @param type the new type
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * sets the transactions of the account
     * @param transactions the new transactions
     */
    public void setTransactions(final List<Transaction> transactions) {
        this.transactions = transactions;
    }

    /**
     * sets the cards of the account
     * @param cards the new cards
     */
    public void setCards(final LinkedList<Card> cards) {
        this.cards = cards;
    }

    /**
     * sets the minimum balance of the account
     * @param minBalance the new minimum balance
     */
    public void setMinBalance(final double minBalance) {
        this.minBalance = minBalance;
    }

    /**
     * sets the currency graph of the account
     * @param currencyGraph the new currency graph
     */
    public void setCurrencyGraph(final CurrencyGraph currencyGraph) {
        this.currencyGraph = currencyGraph;
    }

    /**
     * sets the spendingThreshold map of the account
     * @param spendingThresholdMap the new spendingThreshold map
     */
    public void setSpendingThresholdMap(final Map<String, SpendingThreshold> spendingThresholdMap) {
        this.spendingThresholdMap = spendingThresholdMap;
    }

    /**
     * sets the amount spending threshold of the account
     * @param amountSpendingThreshold the new amount spending threshold
     */
    public void setAmountSpendingThreshold(final double amountSpendingThreshold) {
        this.amountSpendingThreshold = amountSpendingThreshold;
    }

    /**
     * sets the cashback to receive for food
     * @param cashbackToReceiveFood the new cashback to receive for food
     */
    public void setCashbackToReceiveFood(final int cashbackToReceiveFood) {
        this.cashbackToReceiveFood = cashbackToReceiveFood;
    }

    /**
     * sets the cashback to receive for clothes
     * @param cashbackToReceiveClothes the new cashback to receive for clothes
     */
    public void setCashbackToReceiveClothes(final int cashbackToReceiveClothes) {
        this.cashbackToReceiveClothes = cashbackToReceiveClothes;
    }

    /**
     * sets the cashback to receive for tech
     * @param cashbackToReceiveTech the new cashback to receive for tech
     */
    public void setCashbackToReceiveTech(final int cashbackToReceiveTech) {
        this.cashbackToReceiveTech = cashbackToReceiveTech;
    }

    /**
     * computes the output of the account
     * @return the output
     */
    public ObjectNode computeOutput() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode accountNode = objectMapper.createObjectNode();
        accountNode.put("IBAN", iban);
        accountNode.put("balance", balance);
        accountNode.put("currency", currency);
        accountNode.put("type", type);
        return accountNode;
    }

    /**
     * computes the error output of the account when the account is not a savings account
     * @param objectMapper the object mapper
     * @param command the command
     * @return the error output
     */
    public abstract ObjectNode computeErrorOutput(ObjectMapper objectMapper,
                                                  CommandInput command);

    /**
     * adds funds to the account
     * @param amount the amount to add
     */
    public abstract void addFunds(double amount, String userEmail);

    /**
     * adds the payment to the account, exchanging the currency if necessary
     * @param amount the amount to accept
     * @param paymentCurrency the currency of the payment
     * @return the amount accepted
     */
    public abstract double acceptPayment(double amount, String paymentCurrency);

    /**
     * pays, decreasing the balance
     * if necessary, the currency is exchanged
     * @param amount the amount to pay
     * @param paymentCurrency the currency of the payment
     * @return 0 if the payment was successful, 1 if the payment failed
     */
    public abstract int payOnline(double amount, String paymentCurrency);
}
