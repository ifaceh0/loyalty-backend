package com.sts.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sts.entity.UserProfile;
import com.sts.repository.UserProfileRepository;

@Service
public class UserProfileService 
{
	 @Autowired
	    private UserProfileRepository userProfileRepository;

	 public UserProfile saveUserProfile(UserProfile userProfile)
	 {
	        return userProfileRepository.save(userProfile);
	    }

	    public List<UserProfile> getAllUserProfiles()
	    {
	        return userProfileRepository.findAll();
	    }

	    public Optional<UserProfile> getUserProfileById(Long userId)
	    {
	        return userProfileRepository.findById(userId);
	    }

	    public void deleteUserProfile(Long userId)
	    {
	        userProfileRepository.deleteById(userId);
	    }
}

