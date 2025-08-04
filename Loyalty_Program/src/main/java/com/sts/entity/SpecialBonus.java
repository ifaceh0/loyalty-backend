package com.sts.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class SpecialBonus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double points;
    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;
    // $ to Points mapping field for apply for special occasion
    private Double dollartoPointsMapping;  // e.g., 1.0 means $1 = 1 point
}
