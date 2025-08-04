package com.sts.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Shopkeeper_Setting {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "shop_id")
	private Shop shop;

	private double sign_upBonuspoints;
	private String bonusdescription;
	private LocalDate beginDate;
	private LocalDate endDate;
	private double amountOff;
	// $ to Points mapping field
	private Double dollarToPointsMapping;  // e.g., 1.0 means $1 = 1 point
}
