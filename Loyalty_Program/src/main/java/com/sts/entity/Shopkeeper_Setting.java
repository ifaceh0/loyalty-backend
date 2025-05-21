package com.sts.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class Shopkeeper_Setting {
		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @OneToOne
	    @JoinColumn(name = "shop_id")
	    private Shop shop;

	    private double dollarToPointMapping; 		//dollarPerPoint
	    private double milestoneBonusAmount; 	//Coupon ($5) to redeem on next purchase

	    private String specialBonusName;//Diwali bonus
	    private double specialBonusPoints;//$ -> 10 points
	    private LocalDate specialBonusStartDate;
	    private LocalDate specialBonusEndDate;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public Shop getShop() {
			return shop;
		}
		public void setShop(Shop shop) {
			this.shop = shop;
		}
		public double getDollarToPointMapping() {
			return dollarToPointMapping;
		}
		public void setDollarToPointMapping(double dollarToPointMapping) {
			this.dollarToPointMapping = dollarToPointMapping;
		}
		public double getMilestoneBonusAmount() {
			return milestoneBonusAmount;
		}
		public void setMilestoneBonusAmount(double milestoneBonusAmount) {
			this.milestoneBonusAmount = milestoneBonusAmount;
		}
		public String getSpecialBonusName() {
			return specialBonusName;
		}
		public void setSpecialBonusName(String specialBonusName) {
			this.specialBonusName = specialBonusName;
		}
		public double getSpecialBonusPoints() {
			return specialBonusPoints;
		}
		public void setSpecialBonusPoints(double specialBonusPoints) {
			this.specialBonusPoints = specialBonusPoints;
		}
		public LocalDate getSpecialBonusStartDate() {
			return specialBonusStartDate;
		}
		public void setSpecialBonusStartDate(LocalDate specialBonusStartDate) {
			this.specialBonusStartDate = specialBonusStartDate;
		}
		public LocalDate getSpecialBonusEndDate() {
			return specialBonusEndDate;
		}
		public void setSpecialBonusEndDate(LocalDate specialBonusEndDate) {
			this.specialBonusEndDate = specialBonusEndDate;
		}
		
		
}
