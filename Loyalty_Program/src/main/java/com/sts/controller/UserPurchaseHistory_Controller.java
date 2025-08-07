package com.sts.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sts.entity.Shop;
import com.sts.entity.User;
import com.sts.entity.UserPurchase_History;
import com.sts.repository.ShopRepository;
import com.sts.repository.UserRepository;
import com.sts.service.UserPurchaseHistory_Service;

 
@RestController
@RequestMapping("/api/userpurchasehistory")
public class UserPurchaseHistory_Controller
{
	@Autowired
	private UserPurchaseHistory_Service userPurchaseHistoryService;
	
//	    @PostMapping("/purchase-history")
//	    public ResponseEntity<UserPurchase_History> createPurchaseHistory(@RequestBody UserPurchase_History userPurchaseHistory) {
//	        UserPurchase_History savedPurchaseHistory = userPurchaseHistoryService.createUserPurchaseHistory(userPurchaseHistory);
//	        return new ResponseEntity<>(savedPurchaseHistory, HttpStatus.CREATED);
//	    }
//
//	@GetMapping("/fetchAllUserPurchaseHistory")
//	public ResponseEntity<List<UserPurchase_History>> getAllUserPurchaseHistoryList(){
//		List<UserPurchase_History> userPurchaseHistoryList = userPurchaseHistoryService.getAllUserPurchaseHistory();
//		return new ResponseEntity<List<UserPurchase_History>>(userPurchaseHistoryList, HttpStatus.OK);
//	}
//
//	@GetMapping("/fetchUserPurchaseHistoryById/{Id}")
//	public ResponseEntity<UserPurchase_History> getUserPurchaseHistoryById(@PathVariable Long Id){
//		return userPurchaseHistoryService.getUserPurchaseHistoryById(Id)
//				.map(userPurchaseHistory -> ResponseEntity.ok(userPurchaseHistory))
//				.orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
//						.body(null));
//	}
}

