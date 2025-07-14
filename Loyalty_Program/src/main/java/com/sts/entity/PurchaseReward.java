package com.sts.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PurchaseReward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double threshold;
    private double points;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;
}
