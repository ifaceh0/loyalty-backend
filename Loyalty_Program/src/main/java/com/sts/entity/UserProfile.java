package com.sts.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "user_profile")
@IdClass(UserProfileId.class)
@Data
public class UserProfile {
	@Id
    private Long userId;
    @Id
    private Long shopId;

    @ManyToOne
    @JoinColumn(name = "shopId", referencedColumnName = "shopId", insertable = false, updatable = false)
    private Shop shop;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "available_points")
    private Integer availablePoints;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId", insertable = false, updatable = false)
    private User user;
}
