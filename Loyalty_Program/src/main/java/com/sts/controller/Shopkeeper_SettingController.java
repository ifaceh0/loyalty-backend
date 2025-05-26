package com.sts.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sts.entity.Shopkeeper_Setting;
import com.sts.service.Shopkeeper_SettingService;

@RestController
@RequestMapping("/api/shopkeeperSetting")
public class Shopkeeper_SettingController {
	@Autowired
	private Shopkeeper_SettingService settingService;
	
	@PostMapping("/saveShopkeeper")
	public ResponseEntity<Shopkeeper_Setting> createShopesetting(@RequestBody Shopkeeper_Setting ss){
		Shopkeeper_Setting shopkeeperDetails=settingService.saveShopSetting(ss);
		return new ResponseEntity<Shopkeeper_Setting>(shopkeeperDetails, HttpStatus.CREATED);
	}

	@GetMapping
    public List<Shopkeeper_Setting> getAllShopSettings() {
        return settingService.getAllShopSettings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Shopkeeper_Setting> getShopSettingById(@PathVariable Long id) {
        return settingService.getShopSettingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
