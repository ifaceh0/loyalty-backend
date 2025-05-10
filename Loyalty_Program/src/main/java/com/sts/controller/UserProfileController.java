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
        UserProfile savedUserProfile  = userProfileService.saveUserProfile(userProfile);
        return new ResponseEntity<>(savedUserProfile , HttpStatus.CREATED);
    }
	
	@GetMapping
	public ResponseEntity<List<UserProfile>> getAllUserProfiles() 
	{
        List<UserProfile> userProfiles = userProfileService.getAllUserProfiles();
        return new ResponseEntity<>(userProfiles, HttpStatus.OK);
    }

	@GetMapping("/{userId}")
	 public ResponseEntity<UserProfile> getUserProfileById(@PathVariable Long userId)
	{
	        Optional<UserProfile> userProfile = userProfileService.getUserProfileById(userId);
	        return userProfile.map(ResponseEntity::ok)
	                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	 }
	
	@DeleteMapping("/{userId}")
	public ResponseEntity<Void> deleteUserProfile(@PathVariable Long userId)
	{
        userProfileService.deleteUserProfile(userId);
        return ResponseEntity.noContent().build();
    }
	
}
