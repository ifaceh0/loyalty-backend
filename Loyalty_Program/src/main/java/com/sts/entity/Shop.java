package com.sts.entity;

import java.util.Set;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Shop {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shopId;
    private String shopName;
    private String email;
    @Column(name = "phone_number", nullable = false,  unique = true)
    private String phone;
    @Column(name = "company_name", nullable = false, unique = true)
    private String companyName;

    @Column(name = "company_address", nullable = false)
    private String companyAddress;

    @Column(name = "company_email", nullable = false, unique = true)
    private String companyEmail;

    @Column(name = "company_phone", nullable = false, unique = true)
    private String companyPhone;


    @OneToOne(mappedBy = "shop", cascade = CascadeType.ALL)
    private Shopkeeper_Setting shopSetting;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    private Set<User> users;

    private String stripeCustomerId;
}