package com.sts.service;

import java.util.List;
import java.util.Optional;

import com.sts.dto.ShopSignupRequest;
import com.sts.entity.Login;
import com.sts.enums.Role;
import com.sts.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sts.entity.Shop;
import com.sts.repository.ShopRepository;

@Service
public class ShopService {
	@Autowired
	private ShopRepository shopRepository;

	@Autowired
	private LoginRepository loginRepository;

	public Shop createShop(Shop shop) {
		return shopRepository.save(shop);
	}
	
	public List<Shop> getAllShop(){
		return shopRepository.findAll();
	}
	
	public Optional<Shop> getShopById(Long shopId) {
		return shopRepository.findById(shopId);
	}
	
	public void deleteShopById(Long shopId) {
		shopRepository.deleteById(shopId);
	}

//	public Shop createShopkeeperAndLogin(ShopSignupRequest request) {
//		if (shopRepository.existsByEmail(request.getEmail())) {
//			throw new RuntimeException("Email already used !");
//		}
//		if (shopRepository.existsByPhone(request.getPhone())) {
//			throw new RuntimeException("PhoneNumber already used !");
//		}
//
//		// Create Shop entity
//		Shop shop = new Shop();
//		shop.setShopName(request.getShopName());
//		shop.setEmail(request.getEmail());
//		shop.setPhone(request.getPhone());
//		shop.setCompanyEmail(request.getCompanyEmail());
//		shop.setCompanyPhone(request.getCompanyPhone());
//		shop.setCompanyName(request.getCompanyName());
//		shop.setCompanyAddress(request.getCompanyAddress());
//		shopRepository.save(shop);
//
//
//		// Save Login entity
//		Login login = new Login();
//		login.setRole(Role.SHOPKEEPER);
//		login.setEmail(request.getEmail());
//		login.setPhone(request.getPhone());
//		login.setPassword(request.getPassword());
//		loginRepository.save(login);
//		return shop;
//	}
}
