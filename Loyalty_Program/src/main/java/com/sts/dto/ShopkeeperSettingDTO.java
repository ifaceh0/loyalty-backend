package com.sts.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ShopkeeperSettingDTO {
    private Long shopId;
    private double sign_upBonuspoints;
    private List<PurchaseRewardDTO> purchaseRewards;
    private List<MilestoneRewardDTO> milestoneRewards;
    private List<SpecialBonusDTO> specialBonuses;
    private String bonusdescription;
    private LocalDate beginDate;
    private LocalDate endDate;
    private double amountOff;
}
