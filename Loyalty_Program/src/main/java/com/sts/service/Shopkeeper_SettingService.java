package com.sts.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sts.entity.Shopkeeper_Setting;
import com.sts.repository.Shopkeeper_SettingRepository;

@Service
public class Shopkeeper_SettingService {
	
	@Autowired
	Shopkeeper_SettingRepository shopkeeperrepo;
	
	public List<Shopkeeper_Setting> getAllShopSettings() {
        return shopkeeperrepo.findAll();
    }

    public Optional<Shopkeeper_Setting> getShopSettingById(Long id) {
        return shopkeeperrepo.findById(id);
    }

    public Shopkeeper_Setting saveShopSetting(Shopkeeper_Setting shopSetting) {
        return shopkeeperrepo.save(shopSetting);
    }

    public void deleteShopSetting(Long id) {
        shopkeeperrepo.deleteById(id);
    }
}
