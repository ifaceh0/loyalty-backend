package com.sts.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Shopkeeper_Setting {
		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @OneToOne
	    @JoinColumn(name = "shop_id")
	    private Shop shop;

	    private double dollarToPointMapping;//dollarPerPoint
	    private double sign_upBonuspoints;
	    private double milestoneBonusAmount; 	//Coupon ($5) to redeem on next purchase
	    private String specialBonusName;//Diwali bonus
	    private double specialBonusPoints;//$ -> 10 points
	    private LocalDate specialBonusStartDate;
	    private LocalDate specialBonusEndDate;
	    
	    private String bonusdescription;	//Special Bonus Coupon Promotion (Begin Date, End Date, $ off) with description
	    private LocalDate beginDate;
	    private LocalDate endDate;
	    private double amountOff;
	
		//constructor for special Bonus Coupon
//	    public Shopkeeper_Setting(String bonusdescription, LocalDate beginDate, LocalDate endDate, double amountOff) {
//	        this.bonusdescription = bonusdescription;
//	        this.beginDate= beginDate;
//	        this.endDate = endDate;
//	        this.amountOff = amountOff;
//	    }
}
