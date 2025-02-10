### Diaconescu Stefania Clara 323CA 2024-2025

## Proiect Etapa 2 - J. POO Morgan Chase & Co

This project aims to develop a simplified e-banking system that simulates the
functionalities of a modern bank. The system will manage user accounts,
transactions and financial operations while ensuring security, modularity, and
extensibility.

### Project Structure
The simplified e-banking system supports the following core features:

`Bank`
- the main class that represents the bank
- acts as the entry point for all commands and user interactions
- it manages the commands, the users, the accounts, the cards and the

`Users management`
- **_User_** - abstract class that represents a user of the system
- they support commands such as **_print users_** that prints all users'
  information about cards and accounts
- **_set alias_** sets the alias of the receiver users' account
- they can be employees or managers for business accounts
- depending on their role, they can have different permissions

`Cards management`
- **_Card_** - abstract class that represents a card of a user
- there are two types of cards **_ClassicCard_** and **_OneTimePayCard_**
  (card designed for one-time transactions, assuring security for specific payments)
- they support commands such as:
    - **_check card status_** that checks if a card is frozen or not
    - **_create card_** and **_delete card_** that creates and removes an existing
      card from the system

`Transactions management`
- **_Transaction_** - class that represents a transaction between two users
- they are computed based on the commands and the type of the accounts and cards
- they are printed when the command **_print transactions_** is given

`Accounts management`
- **_Account_** - abstract class that represents an account of a user
- there are two types of  **_SavingsAccount_** and **_SpendingAccount_**
- they support commands such as:
    - **_add account_** and **_delete account_** that creates and deletes an account
    - **_add funds_** that allows users to deposit money into an account
    - **_report_** and **_spendings report_** that prints information such as
      balance, currency and the transactions within the interval (spendings report
      is used just for classic accounts)
    - **_send money_** and **_pay online_** are used for transactions between
      accounts. The currency is converted if needed. If the payment is split, all
      the accounts involved need to have enough money. Payments fail if any involved
      account lacks sufficient funds
    - **_split payment_** is used for splitting a payment between multiple accounts.
      If the payment is split, all the accounts involved need to have enough money.
      They can be _custom_ or _equal_ and every account needs to accept or reject the
      payment in order to be processed
    
- the business account is a special account that can be used just by the associates
- it supports two types of reports: **_commerciant report_** and **_transaction report_**.
The first one is used to print the commerciants and the amount of money they received and
the second one is monitoring the transactions between the associates
- for the employees there are limits for the transactions that can be changed just
by the owner of the account

`Commerciants management`
- **_Commerciant_** - class that represents a commerciant
- they offer cashback for the users that pay online
- they have two cashback plans: **_Nr Of Transactions_** and
**_Spending Treshold_**. The first one offers cashback for a number of transactions
depending on the commerciant type and the second one is based on the amounts spent

`Service plans`
- depending on the type, the commission for the transactions is different
- also, the cashback differs for each type of service plan
- to change it, a fee is required (except for the case where the user makes 5
payments while having the silver service plan)

### Design Patterns

- the accounts, business reports and cards are implemented using **_factory pattern_** -
allows easy addition of new account or card types in the future
- the **_BankManagement_** class is instantiated using **_singleton
  pattern_** - ensures the class has only one instance
- the currency graph is implemented using **_visitor pattern_** - ensures
  flexibility in handling multiple currencies without altering the graph structure
- the transactions are computed using **_builder pattern_** - facilitates the
  addition of optional attributes without impacting the logic
- the commands are managed using **_command pattern_** - centralizes the
  management of all user commands improving modularity and simplifying future
  command extensions