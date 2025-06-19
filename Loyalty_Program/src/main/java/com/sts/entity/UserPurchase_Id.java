package com.sts.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.IdClass;

@Embeddable
public class UserPurchase_Id implements Serializable {

	    @Column(name = "user_id")
	    private Long userId;

	    @Column(name = "shop_id")
	    private Long shopId;

	    public UserPurchase_Id() {
	    }

	    public UserPurchase_Id(Long userId, Long shopId) {
	        this.userId = userId;
	        this.shopId = shopId;
	    }

	    public Long getUserId() {
	        return userId;
	    }

	    public void setUserId(Long userId) {
	        this.userId = userId;
	    }

	    public Long getShopId() {
	        return shopId;
	    }

	    public void setShopId(Long shopId) {
	        this.shopId = shopId;
	    }
	    
	    @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (!(o instanceof UserPurchase_Id)) return false;
	        UserPurchase_Id that = (UserPurchase_Id) o;
	        return Objects.equals(getUserId(), that.getUserId()) &&
	               Objects.equals(getShopId(), that.getShopId());
	    }
	    @Override
	    public int hashCode() {
	        return Objects.hash(getUserId(), getShopId());
	    }
}
