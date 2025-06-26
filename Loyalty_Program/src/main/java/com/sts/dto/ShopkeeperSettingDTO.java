package com.sts.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ShopkeeperSettingDTO {
    private Long shopId;
    private double dollarToPointMapping;
    private double sign_upBonuspoints;
    private double milestoneBonusAmount;
    private String specialBonusName;
    private double specialBonusPoints;
    private LocalDate specialBonusStartDate;
    private LocalDate specialBonusEndDate;
    private String bonusdescription;
    private LocalDate beginDate;
    private LocalDate endDate;
    private double amountOff;
}
