package com.sts.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime lastUpdatedDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
    
    
    

//    public Shop getShop() {
//        return shop;
//    }
//
//    public void setShop(Shop shop) {
//        this.shop = shop;
//    }
//    public Set<Shop> getShops() {
//		return shops;
//	}
//
//	public void setShops(Set<Shop> shops) {
//		this.shops = shops;
//	}
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn(name = "shopId")
//    private Set<Shop> shops = new HashSet<>();
//    
//    @ElementCollection
//    @CollectionTable(name = "user_loyaltyPoints", 
//                     joinColumns = @JoinColumn(name = "user_id"))
//    @MapKeyColumn(name = "shop_id")
//    @Column(name = "loyalty_points")
//    private Map<String, Double> userLoyaltyPoints = new HashMap<>();

//    public Map<String, Double> getUserLoyaltyPoints() {
//		return userLoyaltyPoints;
//	}
//
//	public void setUserLoyaltyPoints(Map<String, Double> userLoyaltyPoints) {
//		this.userLoyaltyPoints = userLoyaltyPoints;
//	}
}