package com.sts.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.sts.entity.Shop;
import com.sts.entity.Shopkeeper_Setting;
import com.sts.entity.User;
import com.sts.repository.ShopRepository;
import com.sts.repository.Shopkeeper_SettingRepository;
import com.sts.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sts.entity.UserPurchase_History;
import com.sts.repository.UserPurchaseHistory_Repository;

@Service
public class UserPurchaseHistory_Service 
{
		 @Autowired
		    private UserPurchaseHistory_Repository userPurchaseHistoryRepository;

		@Autowired
			private Shopkeeper_SettingRepository shopkeeperSettingRepository;
		@Autowired
			private UserRepository userRepository;
		@Autowired
			private ShopRepository shopRepository;

//	public UserPurchase_History createUserPurchaseHistory(UserPurchase_History input) {
//		// Fetch the managed User and Shop instances
//		User user = userRepository.findById(input.getUser().getUserId())
//				.orElseThrow(() -> new RuntimeException("User not found"));
//
//		Shop shop = shopRepository.findById(input.getShop().getShopId())
//				.orElseThrow(() -> new RuntimeException("Shop not found"));
////
//		// Create a new history object and set managed references
//		UserPurchase_History purchase = new UserPurchase_History();
//		purchase.setUser(user);
//		purchase.setShop(shop);
//		purchase.setTransactionAmount(input.getTransactionAmount());
//		purchase.setPurchaseDate(LocalDateTime.now());
//
//		Integer pointsEarned = 0;
//
//		Shopkeeper_Setting setting = shopkeeperSettingRepository.findByShop(shop.getShopId());
//		if (setting != null && setting.getDollarToPointsMapping() != null) {
//			LocalDate today = purchase.getPurchaseDate().toLocalDate();
//			boolean isWithinActivePeriod =
//					(setting.getBeginDate() == null || !today.isBefore(setting.getBeginDate())) &&
//							(setting.getEndDate() == null || !today.isAfter(setting.getEndDate()));
//			if (isWithinActivePeriod) {
//				pointsEarned = (int) (purchase.getTransactionAmount() * setting.getDollarToPointsMapping());
//			}
//		}
//
//		purchase.setGivenPoints(pointsEarned);
//		return userPurchaseHistoryRepository.save(purchase);
//	}


	//fetch all user purchase History
	public List<UserPurchase_History> getAllUserPurchaseHistory(){
		return userPurchaseHistoryRepository.findAll();
	}

	public Optional<UserPurchase_History> getUserPurchaseHistoryById(Long id) {
		return userPurchaseHistoryRepository.findById(id);
	}
			
	public void deleteUserPurchaseHistoryById(Long id) {
				userPurchaseHistoryRepository.deleteById(id);
			}

}

