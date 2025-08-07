package com.sts.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sts.entity.UserProfile;
import com.sts.entity.UserProfileId;
import com.sts.service.UserProfileService;

@RestController
@RequestMapping("/api/user-profiles")
public class UserProfileController
{
	@Autowired
	private UserProfileService userProfileService;
	
	@PostMapping
    public ResponseEntity<UserProfile> createUserProfile (@RequestBody UserProfile userProfile)
	{
		try {
			UserProfile savedUserProfile  = userProfileService.saveUserProfile(userProfile);
	        return new ResponseEntity<>(savedUserProfile , HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();  
	        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}       
    }
	
//	@GetMapping
//	public ResponseEntity<List<UserProfile>> getAllUserProfiles()
//	{
//        List<UserProfile> userProfiles = userProfileService.getAllUserProfiles();
//        return new ResponseEntity<>(userProfiles, HttpStatus.OK);
//    }

	@GetMapping("/{userId}/{shopId}")
    public ResponseEntity<UserProfile> getUserProfileById(@PathVariable Long userId, @PathVariable Long shopId) {
        UserProfileId id = new UserProfileId(userId, shopId);
        return userProfileService.getUserProfileById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

//	 @DeleteMapping("/{userId}/{shopId}")
//    public void deleteUserProfile(@PathVariable Long userId, @PathVariable Long shopId) {
//        UserProfileId id = new UserProfileId(userId, shopId);
//        userProfileService.deleteUserProfile(id);
//    }
	
}