package com.sts.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sts.entity.Shop;
import com.sts.service.ShopService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/shop")
public class ShopController {
	@Autowired
	private ShopService shopService;
	
	
	@PostMapping("/saveShop")
	public ResponseEntity<Shop> createShop(@RequestBody Shop shop) {
		Shop shopDetails = shopService.createShop(shop);
		return new ResponseEntity<Shop>(shopDetails, HttpStatus.CREATED);
	}
	
	@GetMapping("/fetchAllShop")
	public ResponseEntity<List<Shop>> getAllShopList(){
		List<Shop> shopList = shopService.getAllShop();
		return new ResponseEntity<List<Shop>>(shopList, HttpStatus.OK);
	}
	
	@GetMapping("/fetchShopById/{shopId}")
	public ResponseEntity<Shop> getShopById(@PathVariable Long shopId){
		return shopService.getShopById(shopId)
				.map(shop -> ResponseEntity.ok(shop))
				.orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(null));	
	}
}
