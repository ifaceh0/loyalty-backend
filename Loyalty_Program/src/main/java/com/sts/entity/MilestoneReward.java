package com.sts.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class MilestoneReward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double threshold;
    private double amount;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;
}
