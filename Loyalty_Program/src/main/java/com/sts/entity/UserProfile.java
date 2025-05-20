package com.sts.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class UserProfile 
{
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long userId;

	    private Integer purchasePoints;

	    @Column(name = "transaction_date")
	    private LocalDateTime transactionDate;
	    
	    @Column(name = "total_points")
	    private Integer totalPoints;
	    
	    @ManyToOne
	    @JoinColumn(name = "shop_id", nullable = false)
	    private Shop shop;

	    public Long getUserId() {
			return userId;
		}

		public void setUserId(Long userId) {
			this.userId = userId;
		}

		public Integer getPurchasePoints() {
			return purchasePoints;
		}

		public void setPurchasePoints(Integer purchasePoints) {
			this.purchasePoints = purchasePoints;
		}

		public LocalDateTime getTransactionDate() {
			return transactionDate;
		}

		public void setTransactionDate(LocalDateTime transactionDate) {
			this.transactionDate = transactionDate;
		}

		public Integer getTotalPoints() {
			return totalPoints;
		}

		public void setTotalPoints(Integer totalPoints) {
			this.totalPoints = totalPoints;
		}


}

