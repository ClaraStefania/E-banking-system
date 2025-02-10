package org.poo.bank;

public final class Constants {
    private Constants() {

    }

    public static final String CLASSIC_ACCOUNT = "classic";
    public static final String SAVINGS_ACCOUNT = "savings";
    public static final String BUSINESS_ACCOUNT = "business";
    public static final String PRINT_USERS = "printUsers";
    public static final String ADD_ACCOUNT = "addAccount";
    public static final String CREATE_CARD = "createCard";
    public static final String CREATE_ONE_TIME_CARD = "createOneTimeCard";
    public static final String CLASSIC_CARD = "classicCard";
    public static final String ONE_TIME_PAY_CARD = "oneTimePayCard";
    public static final String ACTIVE = "active";
    public static final String FROZEN = "frozen";
    public static final String ADD_FUNDS = "addFunds";
    public static final String DELETE_ACCOUNT = "deleteAccount";
    public static final String DELETE_CARD = "deleteCard";
    public static final String PAY_ONLINE = "payOnline";
    public static final String SEND_MONEY = "sendMoney";
    public static final String PRINT_TRANSACTIONS = "printTransactions";
    public static final String CHECK_CARD_STATUS = "checkCardStatus";
    public static final String CARD_IS_FROZEN = "The card is frozen";
    public static final String INSUFFICIENT_FUNDS = "Insufficient funds";
    public static final String CARD_NOT_FOUND = "Card not found";
    public static final String SET_MINIMUM_BALANCE = "setMinimumBalance";
    public static final String SPLIT_PAYMENT = "splitPayment";
    public static final String REPORT = "report";
    public static final String SPENDINGS_REPORT = "spendingsReport";
    public static final String ACCOUNT_NOT_FOUND = "Account not found";
    public static final String CHANGE_INTEREST_RATE = "changeInterestRate";
    public static final String THIS_IS_NOT_A_SAVINGS_ACCOUNT = "This is not a savings account";
    public static final String ADD_INTEREST = "addInterest";
    public static final String SET_ALIAS = "setAlias";
    public static final String REACHED_MINIMUM_AMOUNT = "You have reached the minimum amount "
                                                            + "of funds, the card will be frozen";
    public static final String ACCOUNT_NOT_DELETED = "Account couldn't be deleted - "
                                                        + "see org.poo.transactions for details";
    public static final String ACCOUNT_DELETED = "Account deleted";
    public static final String ACCOUNT_HAS_FUNDS = "Account couldn't be deleted - there are "
                                                    + "funds remaining";
    public static final String CARD_DESTROYED = "The card has been destroyed";
    public static final String WITHDRAW_SAVINGS = "withdrawSavings";
    public static final String GOLD = "gold";
    public static final String SILVER = "silver";
    public static final String STANDARD = "standard";
    public static final String STUDENT = "student";
    public static final String RON = "RON";
    public static final String UPGRADE_PLAN = "upgradePlan";
    public static final String CASH_WITHDRAWAL = "cashWithdrawal";
    public static final String ACCEPT_SPLIT_PAYMENT = "acceptSplitPayment";
    public static final String REJECT_SPLIT_PAYMENT = "rejectSplitPayment";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String ADD_NEW_BUSINESS_ASSOCIATE = "addNewBusinessAssociate";
    public static final String MANAGER = "manager";
    public static final String EMPLOYEE = "employee";
    public static final String CHANGE_SPENDING_LIMIT = "changeSpendingLimit";
    public static final String COMMERCIANT_REPORT = "commerciant";
    public static final String TRANSACTION_REPORT = "transaction";
    public static final String BUSINESS_REPORT = "businessReport";
    public static final String CHANGE_DEPOSIT_LIMIT = "changeDepositLimit";
    public static final int UPGRADE_TO_GOLD_FEE = 350;
    public static final int UPGRADE_TO_GOLD_FROM_SILVER = 250;
    public static final int UPGRADE_TO_SILVER = 100;
    public static final int LIMIT_100 = 100;
    public static final int LIMIT_300 = 300;
    public static final int LIMIT_500 = 500;
    public static final double CASHBACK_GOLD_BIG_AMOUNT = 0.007;
    public static final double CASHBACK_GOLD_MEDIUM_AMOUNT = 0.0055;
    public static final double CASHBACK_GOLD_LOW_AMOUNT = 0.005;
    public static final double CASHBACK_SILVER_BIG_AMOUNT = 0.005;
    public static final double CASHBACK_SILVER_MEDIUM_AMOUNT = 0.004;
    public static final double CASHBACK_SILVER_LOW_AMOUNT = 0.003;
    public static final double CASHBACK_STANDARD_BIG_AMOUNT = 0.0025;
    public static final double CASHBACK_STANDARD_MEDIUM_AMOUNT = 0.002;
    public static final double CASHBACK_STANDARD_LOW_AMOUNT = 0.001;
    public static final double INITIAL_LIMIT = 500;
    public static final double SILVER_COMMISSION = 0.001;
    public static final double STANDARD_COMMISSION = 0.002;
    public static final double MINIMUM_AGE = 21;
    public static final int NR_STANDARD_CLOTHES = 5;
    public static final int NR_STANDARD_TECH = 10;
    public static final int NR_STANDARD_FOOD = 2;
    public static final int NR_OF_SILVER_PAYMENTS = 5;
    public static final int MONTHS = 6;
    public static final int YEAR = 2024;
    public static final double CASHBACK_PERCENTAGE = 0.01;
}
