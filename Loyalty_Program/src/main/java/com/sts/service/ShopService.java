package com.sts.service;

import java.util.List;
import java.util.Optional;

import org.hibernate.FetchNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sts.dto.UserDto;
import com.sts.entity.Shop;
import com.sts.entity.User;
import com.sts.repository.ShopRepository;
import com.sts.repository.UserRepository;

@Service
public class ShopService {
	@Autowired
	private ShopRepository shopRepository;
	//@Autowired
	//private UserRepository userRepository;
	
	public Shop createShop(Shop shop) {
		return shopRepository.save(shop);
	}
	
	public List<Shop> getAllShop(){
		return shopRepository.findAll();
	}
	
	public Optional<Shop> getShopById(Long shopId) {
		return shopRepository.findById(shopId);
	}
	
	public void deleteShopById(Long shopId) {
		shopRepository.deleteById(shopId);
	}
	
	/*public UserDto getPhoneNumber(String phoneNumber)
	{
		User user = userRepository.findByPhoneNumber(phoneNumber);
		UserDto dto = new UserDto();
		dto.setUser_Id(getid);
		dto.setName(get);
		
		
	}*/
	

	/* @GetMapping("/search")
	    public ResponseEntity<?> getUserByPhone(@RequestParam String phone) {
	        Optional<User> user = userRepository.findByPhoneNumber(phone);
	       /* if (user.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
	        }*/
			//return getUserByPhone(phon }
}
