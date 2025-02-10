package org.poo.bank.commands.accountCommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Constants;
import org.poo.bank.User;
import org.poo.bank.Command;
import org.poo.bank.CommandsUtils;
import org.poo.bank.account.Account;
import org.poo.bank.account.BusinessAccount;
import org.poo.bank.businessReport.BusinessReportFactory;
import org.poo.bank.businessReport.BusinessReportInterface;
import org.poo.fileio.CommandInput;

import java.util.List;

public class BusinessReport implements Command {
    private final CommandInput command;
    private final List<User> users;
    private final ObjectMapper objectMapper;
    private final ArrayNode output;

    public BusinessReport(final CommandInput command, final List<User> users,
                          final ArrayNode output) {
        this.command = command;
        this.users = users;
        this.objectMapper = new ObjectMapper();
        this.output = output;
    }

    /**
     * computes the report of the account adding the transactions within the interval
     */
    @Override
    public void execute() {
        String type = command.getType();
        String ibanToFind = command.getAccount();

        Account accountAfterIban = CommandsUtils.getAccountAfterIban(users, ibanToFind);

        // if the account is not found or it is not a business account,
        // it will output an error message
        if (accountAfterIban == null) {
            ObjectNode rootNode = objectMapper.createObjectNode();
            rootNode.put("command", Constants.REPORT);
            ObjectNode reportNode = objectMapper.createObjectNode();
            reportNode.put("timestamp", command.getTimestamp());
            reportNode.put("description", Constants.ACCOUNT_NOT_FOUND);
            rootNode.set("output", reportNode);
            rootNode.put("timestamp", command.getTimestamp());
            output.add(rootNode);
            return;
        }

        if (!accountAfterIban.getType().equals(Constants.BUSINESS_ACCOUNT)) {
            ObjectNode rootNode = objectMapper.createObjectNode();
            rootNode.put("command", Constants.SPENDINGS_REPORT);
            ObjectNode reportNode = objectMapper.createObjectNode();
            reportNode.put("error", "This is not a business account");
            rootNode.set("output", reportNode);
            rootNode.put("timestamp", command.getTimestamp());
            output.add(rootNode);
            return;
        }

        // creates the report
        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("command", Constants.BUSINESS_REPORT);

        BusinessReportFactory businessReportFactory = new BusinessReportFactory();
        BusinessReportInterface report = businessReportFactory.createBusinessReport(type,
                objectMapper, (BusinessAccount) accountAfterIban, ibanToFind);
        ObjectNode reportNode = report.generateReport();

        rootNode.set("output", reportNode);
        rootNode.put("timestamp", command.getTimestamp());
        output.add(rootNode);
    }
}
