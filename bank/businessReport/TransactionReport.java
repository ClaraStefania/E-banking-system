package org.poo.bank.businessReport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Manager;
import org.poo.bank.Employee;
import org.poo.bank.Constants;
import org.poo.bank.account.BusinessAccount;

import java.util.List;

public class TransactionReport implements BusinessReportInterface {
    private final ObjectMapper objectMapper;
    private final BusinessAccount businessAccount;
    private final String iban;

    public TransactionReport(final ObjectMapper objectMapper,
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
        List<Manager> managers = businessAccount.getManagers();
        List<Employee> employees = businessAccount.getEmployees();
        double totalSpent = 0;
        double totalDeposited = 0;

        ObjectNode reportNode = objectMapper.createObjectNode();
        reportNode.put("IBAN", iban);
        reportNode.put("balance", businessAccount.getBalance());
        reportNode.put("currency", businessAccount.getCurrency());
        reportNode.put("spending limit", businessAccount.getPaymentLimit());
        reportNode.put("deposit limit",  businessAccount.getDepositLimit());
        reportNode.put("statistics type", Constants.TRANSACTION_REPORT);

        ArrayNode managersNode = objectMapper.createArrayNode();
        for (Manager manager : managers) {
            ObjectNode managerNode = objectMapper.createObjectNode();
            managerNode.put("username", manager.getUser().getLastName() + " "
                    + manager.getUser().getFirstName());
            managerNode.put("spent", manager.getSpent());
            managerNode.put("deposited", manager.getDeposited());
            managersNode.add(managerNode);

            totalSpent += manager.getSpent();
            totalDeposited += manager.getDeposited();
        }

        // adds the payments of the employees
        ArrayNode employeesNode = objectMapper.createArrayNode();
        for (Employee employee : employees) {
            ObjectNode employeeNode = objectMapper.createObjectNode();
            employeeNode.put("username", employee.getUser().getLastName() + " "
                    + employee.getUser().getFirstName());
            employeeNode.put("spent", employee.getSpent());
            employeeNode.put("deposited", employee.getDeposited());
            employeesNode.add(employeeNode);

            totalSpent += employee.getSpent();
            totalDeposited += employee.getDeposited();
        }

        reportNode.set("managers", managersNode);
        reportNode.set("employees", employeesNode);
        reportNode.put("total spent", totalSpent);
        reportNode.put("total deposited", totalDeposited);
        return reportNode;
    }
}
