package com.sts.dto;

import lombok.Data;

import java.util.List;

@Data
public class SubscriptionDetails {
    private String email;
    private String status;
    private String planName;
    private String interval;
    private String nextPlanName;
    private String nextInterval;
    private Double price;
    private List applications;
    private String startDate;
    private String endDate;
    private Boolean autoRenew;
    private Boolean cancelAtPeriodEnd;
    private String stripeCustomerId;
    private String stripeSubscriptionId;

    public SubscriptionDetails(
            String email, String status, String planName, String interval, Double price, List applications,
            String nextPlanName, String nextInterval, String startDate, String endDate,
            Boolean autoRenew, Boolean cancelAtPeriodEnd, String stripeCustomerId, String stripeSubscriptionId
    ) {
        this.email = email;
        this.status = status;
        this.planName = planName;
        this.interval = interval;
        this.price = price;
        this.applications = applications;
        this.nextPlanName = nextPlanName;
        this.nextInterval = nextInterval;
        this.startDate = startDate;
        this.endDate = endDate;
        this.autoRenew = autoRenew;
        this.cancelAtPeriodEnd = cancelAtPeriodEnd;
        this.stripeCustomerId = stripeCustomerId;
        this.stripeSubscriptionId = stripeSubscriptionId;
    }
}
