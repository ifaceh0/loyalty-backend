package com.sts.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sts.entity.Shop;
import com.sts.repository.ShopRepository;

@Service
public class ShopService {
	@Autowired
	private ShopRepository shopRepository;
	
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
}
