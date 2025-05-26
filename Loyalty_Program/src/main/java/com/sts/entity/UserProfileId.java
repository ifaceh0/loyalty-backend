package com.sts.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * UserProfileId is a composite key class for the UserProfile entity.
 * It uniquely identifies a UserProfile by combining userId and shopId.
 *
 * This allows the application to track a user's profile (such as points and transactions)
 * for each shop separately, ensuring that each user can have only one profile per shop.
 *
 * Fields:
 *   - userId: The unique identifier of the user (foreign key to User)
 *   - shopId: The unique identifier of the shop (foreign key to Shop)
 *
 * Used with @IdClass(UserProfileId.class) in UserProfile entity.
 *
 * Standard equals and hashCode implementations are provided for correct behavior in JPA.
 */
public class UserProfileId implements Serializable {
    private Long userId;
    private Long shopId;
    public UserProfileId() {}
    public UserProfileId(Long userId, Long shopId) {
        this.userId = userId;
        this.shopId = shopId;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfileId that = (UserProfileId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(shopId, that.shopId);
    }
    @Override
    public int hashCode() {
        return Objects.hash(userId, shopId);
    }
    // Getters and setters (optional, but can be added if needed)
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getShopId() { return shopId; }
    public void setShopId(Long shopId) { this.shopId = shopId; }
}

