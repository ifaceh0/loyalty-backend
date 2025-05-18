package com.sts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sts.entity.Shopkeeper_Setting;
import com.sts.repository.Shopkeeper_SettingRepository;

@Service
public class Shopkeeper_SettingService {
	
	@Autowired
	Shopkeeper_SettingRepository shopkeeperrepo;
	
	public Shopkeeper_Setting createShopkeeper(Shopkeeper_Setting shopsave) {
		return shopkeeperrepo.save(shopsave);
	}
}
