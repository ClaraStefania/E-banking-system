package org.poo.bank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

public final class Transaction {
    private final String senderIban;
    private final String receiverIban;
    private final double amount;
    private final String currency;
    private final String transferType;
    private final String description;
    private final int timestamp;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String type;
    private final String cardNumber;
    private final String cardHolder;
    private final String account;
    private final String commerciant;
    private final double cardPayment;
    private final List<String> involvedAccounts;
    private final String splitCurrency;
    private final String error;
    private final String accountIban;
    private final String newPlanType;
    private final String splitPaymentType;
    private final List<Double> amountForUsers;
    private final double setAmountEqualSplitPayment;
    private final String savingsAccountIban;
    private final String classicAccountIban;

    private Transaction(final Builder builder) {
        this.senderIban = builder.senderIban;
        this.receiverIban = builder.receiverIban;
        this.amount = builder.amount;
        this.currency = builder.currency;
        this.transferType = builder.transferType;
        this.description = builder.description;
        this.timestamp = builder.timestamp;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.type = builder.type;
        this.cardNumber = builder.cardNumber;
        this.cardHolder = builder.cardHolder;
        this.account = builder.account;
        this.commerciant = builder.commerciant;
        this.cardPayment = builder.cardPayment;
        this.involvedAccounts = builder.involvedAccounts;
        this.splitCurrency = builder.splitCurrency;
        this.error = builder.error;
        this.accountIban = builder.accountIban;
        this.newPlanType = builder.newPlanType;
        this.splitPaymentType = builder.splitPaymentType;
        this.amountForUsers = builder.amountForUsers;
        this.setAmountEqualSplitPayment = builder.setAmountEqualSplitPayment;
        this.savingsAccountIban = builder.savingsAccountIban;
        this.classicAccountIban = builder.classicAccountIban;
    }

    /**
     * gets the sender's IBAN
     * @return the value of senderIban
     */
    public String getSenderIban() {
        return senderIban;
    }

    /**
     * gets the receiver's IBAN
     * @return the value of receiverIban
     */
    public String getReceiverIban() {
        return receiverIban;
    }

    /**
     * gets the amount of the transaction
     * @return the value of amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * gets the currency of the transaction
     * @return the value of currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * gets the transfer type of the transfer
     * @return the value of transferType
     */
    public String getTransferType() {
        return transferType;
    }

    /**
     * gets the description of the transaction
     * @return the value of description
     */
    public String getDescription() {
        return description;
    }

    /**
     * gets the timestamp of the transaction
     * @return the value of timestamp
     */
    public int getTimestamp() {
        return timestamp;
    }

    /**
     * gets the first name of the user
     * @return the value of firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * gets the last name of the user
     * @return the value of lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * gets the email of the user
     * @return the value of email
     */
    public String getEmail() {
        return email;
    }

    /**
     * gets the type of the card
     * @return the value of type
     */
    public String getType() {
        return type;
    }

    /**
     * gets the card number
     * @return the value of cardNumber
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * gets the cardholder
     * @return the value of cardHolder
     */
    public String getCardHolder() {
        return cardHolder;
    }

    /**
     * gets the iban
     * @return the value of iban
     */
    public String getAccount() {
        return account;
    }

    /**
     * gets the commerciant
     * @return the value of commerciant
     */
    public String getCommerciant() {
        return commerciant;
    }

    /**
     * gets the card payment
     * @return the value of cardPayment
     */
    public double getCardPayment() {
        return cardPayment;
    }

    /**
     * gets the involved accounts
     * @return the value of involvedAccounts
     */
    public List<String> getInvolvedAccounts() {
        return involvedAccounts;
    }

    /**
     * gets the split currency
     * @return the value of splitCurrency
     */
    public String getSplitCurrency() {
        return splitCurrency;
    }

    /**
     * gets the error
     * @return the value of error
     */
    public String getError() {
        return error;
    }

    /**
     * gets the account's IBAN
     * @return the value of accountIban
     */
    public String getAccountIban() {
        return accountIban;
    }

    /**
     * gets the new plan type
     * @return the value of newPlanType
     */
    public String getNewPlanType() {
        return newPlanType;
    }

    /**
     * gets the split payment type
     * @return the value of splitPaymentType
     */
    public String getSplitPaymentType() {
        return splitPaymentType;
    }

    /**
     * gets the amount for users
     * @return the value of amountForUsers
     */
    public List<Double> getAmountForUsers() {
        return amountForUsers;
    }

    /**
     * gets the amount for users
     * @return the value of amountForUsers
     */
    public double getSetAmountEqualSplitPayment() {
        return setAmountEqualSplitPayment;
    }

    /**
     * gets the savings account's IBAN
     * @return the value of savingsAccountIban
     */
    public String getSavingsAccountIban() {
        return savingsAccountIban;
    }

    /**
     * gets the classic account's IBAN
     * @return the value of classicAccountIban
     */
    public String getClassicAccountIban() {
        return classicAccountIban;
    }

    public static class Builder {
        private String senderIban;
        private String receiverIban;
        private double amount;
        private String currency;
        private String transferType;
        private String description;
        private int timestamp;
        private String firstName;
        private String lastName;
        private String email;
        private String type;
        private String cardNumber;
        private String account;
        private String cardHolder;
        private String commerciant;
        private double cardPayment;
        private List<String> involvedAccounts;
        private String splitCurrency;
        private String error;
        private String accountIban;
        private String newPlanType;
        private String splitPaymentType;
        private List<Double> amountForUsers;
        private double setAmountEqualSplitPayment;
        private String savingsAccountIban;
        private String classicAccountIban;

        /**
         * sets the sender's IBAN
         * @param senderIbanBuilder the value to be set
         * @return the builder
         */
        public Builder setSenderIban(final String senderIbanBuilder) {
            this.senderIban = senderIbanBuilder;
            return this;
        }

        /**
         * sets the receiver's IBAN
         * @param receiverIbanBuilder the value to be set
         * @return the builder
         */
        public Builder setReceiverIban(final String receiverIbanBuilder) {
            this.receiverIban = receiverIbanBuilder;
            return this;
        }

        /**
         * sets the amount of the transaction
         * @param amountBuilder the value to be set
         * @return the builder
         */
        public Builder setAmount(final double amountBuilder) {
            this.amount = amountBuilder;
            return this;
        }

        /**
         * sets the currency of the transaction
         * @param currencyBuilder the value to be set
         * @return the builder
         */
        public Builder setCurrency(final String currencyBuilder) {
            this.currency = currencyBuilder;
            return this;
        }

        /**
         * sets the transfer type of the transaction
         * @param transferTypeBuilder the value to be set
         * @return the builder
         */
        public Builder setTransferType(final String transferTypeBuilder) {
            this.transferType = transferTypeBuilder;
            return this;
        }

        /**
         * sets the description of the transaction
         * @param descriptionBuilder the value to be set
         * @return the builder
         */
        public Builder setDescription(final String descriptionBuilder) {
            this.description = descriptionBuilder;
            return this;
        }

        /**
         * sets the timestamp of the transaction
         * @param timestampBuilder the value to be set
         * @return the builder
         */
        public Builder setTimestamp(final int timestampBuilder) {
            this.timestamp = timestampBuilder;
            return this;
        }

        /**
         * sets the first name of the user
         * @param firstNameBuilder the value to be set
         * @return the builder
         */
        public Builder setFirstName(final String firstNameBuilder) {
            this.firstName = firstNameBuilder;
            return this;
        }

        /**
         * sets the last name of the user
         * @param lastNameBuilder the value to be set
         * @return the builder
         */
        public Builder setLastName(final String lastNameBuilder) {
            this.lastName = lastNameBuilder;
            return this;
        }

        /**
         * sets the email of the user
         * @param emailBuilder the value to be set
         * @return the builder
         */
        public Builder setEmail(final String emailBuilder) {
            this.email = emailBuilder;
            return this;
        }

        /**
         * sets the type of the card
         * @param typeBuilder the value to be set
         * @return the builder
         */
        public Builder setType(final String typeBuilder) {
            this.type = typeBuilder;
            return this;
        }

        /**
         * sets the card number
         * @param cardNumberBuilder the value to be set
         * @return the builder
         */
        public Builder setCardNumber(final String cardNumberBuilder) {
            this.cardNumber = cardNumberBuilder;
            return this;
        }

        /**
         * sets the cardholder
         * @param cardHolderBuilder the value to be set
         * @return the builder
         */
        public Builder setCardHolder(final String cardHolderBuilder) {
            this.cardHolder = cardHolderBuilder;
            return this;
        }

        /**
         * sets the account
         * @param accountBuilder the value to be set
         * @return the builder
         */
        public Builder setAccount(final String accountBuilder) {
            this.account = accountBuilder;
            return this;
        }

        /**
         * sets the commerciant
         * @param commerciantBuilder the value to be set
         * @return the builder
         */
        public Builder setCommerciant(final String commerciantBuilder) {
            this.commerciant = commerciantBuilder;
            return this;
        }

        /**
         * sets the card payment
         * @param cardPaymentBuilder the value to be set
         * @return the builder
         */
        public Builder setCardPayment(final double cardPaymentBuilder) {
            this.cardPayment = cardPaymentBuilder;
            return this;
        }

        /**
         * sets the involved accounts
         * @param involvedAccountsBuilder the value to be set
         * @return the builder
         */
        public Builder setInvolvedAccounts(final List<String> involvedAccountsBuilder) {
            this.involvedAccounts = involvedAccountsBuilder;
            return this;
        }

        /**
         * sets the split currency
         * @param splitCurrencyBuilder the value to be set
         * @return the builder
         */
        public Builder setSplitCurrency(final String splitCurrencyBuilder) {
            this.splitCurrency = splitCurrencyBuilder;
            return this;
        }

        /**
         * sets the error
         * @param errorBuilder the value to be set
         * @return the builder
         */
        public Builder setError(final String errorBuilder) {
            this.error = errorBuilder;
            return this;
        }

        /**
         * sets the account's IBAN
         * @param accountIbanBuilder the value to be set
         * @return the builder
         */
        public Builder setAccountIban(final String accountIbanBuilder) {
            this.accountIban = accountIbanBuilder;
            return this;
        }

        /**
         * sets the new plan type
         * @param newPlanTypeBuilder the value to be set
         * @return the builder
         */
        public Builder setNewPlanType(final String newPlanTypeBuilder) {
            this.newPlanType = newPlanTypeBuilder;
            return this;
        }

        /**
         * sets the split payment type
         * @param splitPaymentTypeBuilder the value to be set
         * @return the builder
         */
        public Builder setSplitPaymentType(final String splitPaymentTypeBuilder) {
            this.splitPaymentType = splitPaymentTypeBuilder;
            return this;
        }

        /**
         * sets the amount for users
         * @param amountForUsersBuilder the value to be set
         * @return the builder
         */
        public Builder setAmountForUsers(final List<Double> amountForUsersBuilder) {
            this.amountForUsers = amountForUsersBuilder;
            return this;
        }

        /**
         * sets the amount for users
         * @param setAmountEqualSplitPaymentBuilder the value to be set
         * @return the builder
         */
        public Builder setAmountEqualSplitPayment(final double setAmountEqualSplitPaymentBuilder) {
            this.setAmountEqualSplitPayment = setAmountEqualSplitPaymentBuilder;
            return this;
        }

        /**
         * sets the savings account's IBAN
         * @param savingsAccountIbanBuilder the value to be set
         * @return the builder
         */
        public Builder setSavingsAccountIban(final String savingsAccountIbanBuilder) {
            this.savingsAccountIban = savingsAccountIbanBuilder;
            return this;
        }

        /**
         * sets the classic account's IBAN
         * @param classicAccountIbanBuilder the value to be set
         * @return the builder
         */
        public Builder setClassicAccountIban(final String classicAccountIbanBuilder) {
            this.classicAccountIban = classicAccountIbanBuilder;
            return this;
        }

        /**
         * builds the transaction
         * @return the transaction
         */
        public Transaction build() {
            return new Transaction(this);
        }
    }

    /**
     * computes the transaction output
     * @param objectMapper the object mapper
     * @param transaction the transaction
     * @return the transaction output
     */
    public ObjectNode computeTransactionOutput(final ObjectMapper objectMapper,
                                               final Transaction transaction) {
        ObjectNode transactionNode = objectMapper.createObjectNode();
        transactionNode.put("timestamp", transaction.getTimestamp());
        transactionNode.put("description", transaction.getDescription());

        if (transaction.getSenderIban() != null) {
            transactionNode.put("senderIBAN", transaction.getSenderIban());
        }

        if (transaction.getReceiverIban() != null) {
            transactionNode.put("receiverIBAN", transaction.getReceiverIban());
        }

        if (transaction.getAmount() > 0) {
            transactionNode.put("amount", transaction.getAmount() + " "
                    + transaction.getCurrency());
        }

        if (transaction.getTransferType() != null) {
            transactionNode.put("transferType", transaction.getTransferType());
        }

        if (transaction.getCardNumber() != null) {
            transactionNode.put("card", transaction.getCardNumber());
        }

        if (transaction.getCardHolder() != null) {
            transactionNode.put("cardHolder", transaction.getCardHolder());
        }

        if (transaction.getAccount() != null) {
            transactionNode.put("account", transaction.getAccount());
        }

        if (transaction.getAccountIban() != null) {
            transactionNode.put("accountIBAN", transaction.getAccountIban());
        }

        if (transaction.getSavingsAccountIban() != null) {
            transactionNode.put("savingsAccountIBAN", transaction.getSavingsAccountIban());
        }

        if (transaction.getClassicAccountIban() != null) {
            transactionNode.put("classicAccountIBAN", transaction.getClassicAccountIban());
        }

        if (transaction.getCardPayment() > 0) {
            transactionNode.put("amount", transaction.getCardPayment());
        }

        if (transaction.getSplitPaymentType() != null) {
            transactionNode.put("splitPaymentType", transaction.getSplitPaymentType());
        }

        if (transaction.getSplitCurrency() != null) {
            transactionNode.put("currency", transaction.getSplitCurrency());
        }

        if (transaction.getSetAmountEqualSplitPayment() > 0) {
            transactionNode.put("amount", transaction.getSetAmountEqualSplitPayment());
        }

        if (transaction.getCommerciant() != null) {
            transactionNode.put("commerciant", transaction.getCommerciant());
        }

        if (transaction.getAmountForUsers() != null) {
            ArrayNode amountForUsersArray = objectMapper.createArrayNode();
            for (double userAmount : transaction.getAmountForUsers()) {
                amountForUsersArray.add(userAmount);
            }
            transactionNode.set("amountForUsers", amountForUsersArray);
        }

        if (transaction.getInvolvedAccounts() != null) {
            ArrayNode accountsArray = objectMapper.createArrayNode();
            for (String iban : transaction.getInvolvedAccounts()) {
                accountsArray.add(iban);
            }
            transactionNode.set("involvedAccounts", accountsArray);
        }

        if (transaction.getError() != null) {
            transactionNode.put("error", transaction.getError());
        }

        if (transaction.getNewPlanType() != null) {
            transactionNode.put("newPlanType", transaction.getNewPlanType());
        }

        return transactionNode;
    }
}
