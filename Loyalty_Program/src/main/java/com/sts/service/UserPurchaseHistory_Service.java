package com.sts.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sts.entity.UserPurchase_History;
import com.sts.repository.UserPurchaseHistory_Repository;

@Service
public class UserPurchaseHistory_Service 
{
		 @Autowired
		    private UserPurchaseHistory_Repository userPurchaseHistoryRepository;
			
			public UserPurchase_History createUserPurchaseHistory(UserPurchase_History userPurchaseHistory) {
				return userPurchaseHistoryRepository.save(userPurchaseHistory);
			}
			
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

