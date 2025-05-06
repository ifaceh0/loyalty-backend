package com.sts.entity;

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

    private double purchase; 		//dollarPerPoint
    private String reward; 			// e.g., "100 points = $10 off"
    private String promotionCoupon; // e.g., "$50 off on next purchase after $5000 spent"
    private int newUserBonusPoints;
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
	public double getPurchase() {
		return purchase;
	}
	public void setPurchase(double purchase) {
		this.purchase = purchase;
	}
	public String getReward() {
		return reward;
	}
	public void setReward(String reward) {
		this.reward = reward;
	}
	public String getPromotionCoupon() {
		return promotionCoupon;
	}
	public void setPromotionCoupon(String promotionCoupon) {
		this.promotionCoupon = promotionCoupon;
	}
	public int getNewUserBonusPoints() {
		return newUserBonusPoints;
	}
	public void setNewUserBonusPoints(int newUserBonusPoints) {
		this.newUserBonusPoints = newUserBonusPoints;
	}

}
