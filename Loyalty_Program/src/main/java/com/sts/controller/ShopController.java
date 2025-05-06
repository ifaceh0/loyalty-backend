package com.sts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sts.entity.Shop;
import com.sts.service.ShopService;
import org.springframework.web.bind.annotation.PostMapping;


@RestController("shop")
public class ShopController {
	@Autowired
	private ShopService shopService;
	
	@PostMapping("saveShop")
	public ResponseEntity<Shop> createShop(@RequestBody Shop shop) {
		Shop shopDetails = shopService.createShop(shop);
		return new ResponseEntity<Shop>(shopDetails, HttpStatus.CREATED);
	}
	
	
}
