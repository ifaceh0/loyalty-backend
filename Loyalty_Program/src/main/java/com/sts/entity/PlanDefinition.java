package com.sts.entity;

import java.time.LocalDateTime;

import com.sts.enums.PaymentTerm;
import com.sts.enums.PlanType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "plan_definition")
@Data
public class PlanDefinition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PlanType planType;

    @Enumerated(EnumType.STRING)
    private PaymentTerm paymentTerm; // MONTHLY, QUARTERLY, YEARLY

    private double price;
    private String stripePriceId;

    private boolean active;

    @Column(name = "duration_in_months")
    private int durationInMonths;

    private LocalDateTime effectiveFrom;
    private LocalDateTime createdOn;
}