package com.sts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sts.entity.Shop;
import com.sts.entity.Shopkeeper_Setting;
import com.sts.repository.ShopRepository;
import com.sts.repository.Shopkeeper_SettingRepository;

@Service
public class ShopService {
	@Autowired
	private ShopRepository shopRepository;
	
	@Autowired
	Shopkeeper_SettingRepository shopkeeperrepo;
	
	public Shop createShop(Shop shop) {
		return shopRepository.save(shop);
	}
	public Shopkeeper_Setting createShopkeeper(Shopkeeper_Setting shopsave) {
		return shopkeeperrepo.save(shopsave);
	}
}
