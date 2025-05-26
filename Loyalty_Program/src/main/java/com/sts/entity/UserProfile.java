package com.sts.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * The UserProfile entity represents a user's loyalty profile for a specific shop.
 * It tracks the user's points, transactions, and related data for each shop they participate in.
 *
 * Composite Key:
 *   - userId: The unique identifier of the user (part of the composite key)
 *   - shopId: The unique identifier of the shop (part of the composite key)
 *   (Composite key is defined by @IdClass(UserProfileId.class))
 *
 * Fields:
 *   - userId: User's unique ID (foreign key to User)
 *   - shopId: Shop's unique ID (foreign key to Shop)
 *   - shop: Reference to the Shop entity
 *   - purchasePoints: Points earned in a transaction
 *   - transactionDate: Date and time of the transaction
 *   - totalPoints: User's total points in this shop
 *
 * Relationships:
 *   - Many user profiles can reference the same shop (ManyToOne)
 *
 * This entity is mapped to the 'user_profile' table in the database.
 */
@Entity

@Table(name = "user_profile")
@IdClass(UserProfileId.class)
public class UserProfile {
	@Id
    private Long userId;
    @Id
    private Long shopId;

    @ManyToOne
    @JoinColumn(name = "shopId", referencedColumnName = "shopId", insertable = false, updatable = false)
    private Shop shop;

    private Integer purchasePoints;

    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;

    @Column(name = "total_points")
    private Integer totalPoints;

    // Getters and setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getShopId() { return shopId; }
    public void setShopId(Long shopId) { this.shopId = shopId; }
    public Shop getShop() { return shop; }
    public void setShop(Shop shop) { this.shop = shop; }
    public Integer getPurchasePoints() { return purchasePoints; }
    public void setPurchasePoints(Integer purchasePoints) { this.purchasePoints = purchasePoints; }
    public LocalDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }
    public Integer getTotalPoints() { return totalPoints; }
    public void setTotalPoints(Integer totalPoints) { this.totalPoints = totalPoints; }
}
