package org.poo.bank.businessReport;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.poo.bank.Constants;
import org.poo.bank.account.BusinessAccount;

public class BusinessReportFactory {
    /**
     * Creates a business report based on the given type.
     * @param type the type of the report
     * @param objectMapper the object mapper used to create JSON objects
     * @param account the account for which the report is created
     * @param iban the IBAN of the account
     * @return the created business report
     */
    public BusinessReportInterface createBusinessReport(final String type,
                                                        final ObjectMapper objectMapper,
                                                        final BusinessAccount account,
                                                        final String iban) {
        return switch (type) {
            case Constants.COMMERCIANT_REPORT -> new CommerciantReport(objectMapper, account, iban);
            case Constants.TRANSACTION_REPORT -> new TransactionReport(objectMapper, account, iban);
            default -> throw new IllegalArgumentException("The report type " + type
                    + " is not recognized.");
        };
    }
}
