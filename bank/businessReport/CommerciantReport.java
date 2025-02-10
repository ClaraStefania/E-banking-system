package org.poo.bank.businessReport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Commerciant;
import org.poo.bank.Constants;
import org.poo.bank.Employee;
import org.poo.bank.Manager;
import org.poo.bank.account.BusinessAccount;

import java.util.List;

public class CommerciantReport implements BusinessReportInterface {
    private final ObjectMapper objectMapper;
    private final BusinessAccount businessAccount;
    private final String iban;

    public CommerciantReport(final ObjectMapper objectMapper,
                             final BusinessAccount businessAccount, final String iban) {
        this.objectMapper = objectMapper;
        this.businessAccount = businessAccount;
        this.iban = iban;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectNode generateReport() {
        List<Commerciant> commerciants = businessAccount.getCommerciants();
        ObjectNode reportNode = objectMapper.createObjectNode();
        reportNode.put("IBAN", iban);
        reportNode.put("balance", businessAccount.getBalance());
        reportNode.put("currency", businessAccount.getCurrency());
        reportNode.put("spending limit", businessAccount.getPaymentLimit());
        reportNode.put("deposit limit",  businessAccount.getDepositLimit());
        reportNode.put("statistics type", Constants.COMMERCIANT_REPORT);

        ArrayNode commerciantsNode = objectMapper.createArrayNode();
        for (Commerciant commerciant : commerciants) {
            ObjectNode commerciantNode = objectMapper.createObjectNode();
            commerciantNode.put("commerciant", commerciant.getCommerciantName());
            commerciantNode.put("total received", commerciant.getAmount());

            // adds the names of the managers
            ArrayNode managerNamesNode = objectMapper.createArrayNode();
            for (Manager manager : commerciant.getManagers()) {
                String managerName = manager.getUser().getLastName() + " "
                        + manager.getUser().getFirstName();
                managerNamesNode.add(managerName);
            }
            commerciantNode.set("managers", managerNamesNode);

            // adds the names of the employees
            ArrayNode employerNamesNode = objectMapper.createArrayNode();
            for (Employee employer : commerciant.getEmployees()) {
                String employerName = employer.getUser().getLastName() + " "
                        + employer.getUser().getFirstName();
                employerNamesNode.add(employerName);
            }
            commerciantNode.set("employees", employerNamesNode);

            commerciantsNode.add(commerciantNode);
        }

        reportNode.set("commerciants", commerciantsNode);
        return reportNode;
    }
}
