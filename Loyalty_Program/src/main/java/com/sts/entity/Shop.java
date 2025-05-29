package com.sts.entity;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Shop {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shopId;
    private String shopName;
    private String shopOwner;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private String email;

    @OneToOne(mappedBy = "shop", cascade = CascadeType.ALL)
    private Shopkeeper_Setting shopSetting;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    private Set<User> users;

    private String stripeCustomerId;

    // Getters and setters
    public Long getShopId() { return shopId; }
    public void setShopId(Long shopId) { this.shopId = shopId; }
    public String getShopName() { return shopName; }
    public void setShopName(String shopName) { this.shopName = shopName; }
    public String getShopOwner() { return shopOwner; }
    public void setShopOwner(String shopOwner) { this.shopOwner = shopOwner; }
    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Set<User> getUsers() { return users; }
    public void setUsers(Set<User> users) { this.users = users; }
    public String getStripeCustomerId() {return stripeCustomerId;}
    public void setStripeCustomerId(String stripeCustomerId) {this.stripeCustomerId = stripeCustomerId;}
}