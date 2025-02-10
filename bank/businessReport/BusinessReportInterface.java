package org.poo.bank.businessReport;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface BusinessReportInterface {
    /**
     * Generates a report for the account.
     * @return the generated report
     */
    ObjectNode generateReport();
}
