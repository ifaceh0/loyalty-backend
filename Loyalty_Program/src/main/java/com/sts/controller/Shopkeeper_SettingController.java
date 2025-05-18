package com.sts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
		Shopkeeper_Setting shopkeeperDetails=settingService.createShopkeeper(ss);
		return new ResponseEntity<Shopkeeper_Setting>(shopkeeperDetails, HttpStatus.CREATED);
	}
}
