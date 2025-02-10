package org.poo.bank.servicePlan;

import org.poo.bank.Constants;

public class ServicePlanFactory {
    /**
     * creates a service plan
     * @param type the type of the plan
     * @return the plan
     */
    public ServicePlan createServiceplan(final String type) {
        return switch (type) {
            case Constants.GOLD -> new GoldPlan();
            case Constants.SILVER -> new SilverPlan();
            case Constants.STANDARD -> new StandardPlan();
            case Constants.STUDENT -> new StudentPlan();
            default -> throw new IllegalArgumentException("The type " + type
                    + " is not recognized.");
        };
    }
}
