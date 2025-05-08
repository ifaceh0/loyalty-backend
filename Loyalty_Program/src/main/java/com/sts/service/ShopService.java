package com.sts.service;

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
}
