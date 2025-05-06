package com.sts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sts.entity.Shop;
import com.sts.repository.ShopRepository;

@RestController
public class ShopController {
	@Autowired
	private ShopRepository shopRepository;
	
	
	
}
