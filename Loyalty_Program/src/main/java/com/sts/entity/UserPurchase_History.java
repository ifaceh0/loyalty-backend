package com.sts.entity;

import java.time.LocalDateTime;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class UserPurchase_History {
	@EmbeddedId
    private UserPurchase_Id purchaseId;
	
	// Many purchase history records belong to one User
    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
    // Many purchase history records belong to one Shop
    @ManyToOne
    @JoinColumn(name = "shop_id", insertable = false, updatable = false)
    private Shop shop;

    // Optional: timestamp of purchase
    private LocalDateTime purchaseDate;

    private Integer transactionAmount;

    
    // Getters and Setters 
//
//	public UserPurchase_Id getPurchaseId() {
//		return purchaseId;
//	}
//
//	public void setPurchaseId(UserPurchase_Id purchaseId) {
//		this.purchaseId = purchaseId;
//	}
//
//	public LocalDateTime getPurchaseDate() {
//		return purchaseDate;
//	}
//
//	public void setPurchaseDate(LocalDateTime purchaseDate) {
//		this.purchaseDate = purchaseDate;
//	}
//
//	public Integer getTransactionAmount() {
//		return transactionAmount;
//	}
//
//	public void setTransactionAmount(Integer transactionAmount) {
//		this.transactionAmount = transactionAmount;
//	}
}
