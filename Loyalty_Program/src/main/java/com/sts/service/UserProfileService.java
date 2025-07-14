package com.sts.service;

import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sts.entity.UserProfile;
import com.sts.entity.UserProfileId;
import com.sts.repository.UserProfileRepository;

@Service
public class UserProfileService {
    @Autowired
    private UserProfileRepository userProfileRepository;

    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public List<UserProfile> getAllUserProfiles() {
        return userProfileRepository.findAll();
    }

    public Optional<UserProfile> getUserProfileById(UserProfileId id) {
        return userProfileRepository.findById(id);
    }

    public UserProfile saveUserProfile(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    public void deleteUserProfile(UserProfileId id) {
        userProfileRepository.deleteById(id);
    }

    @Transactional
    public void updateAvailablePoints(Long userId, Long shopId, int pointsToAdd) {
        Optional<UserProfile> optionalProfile = Optional.ofNullable(userProfileRepository.findByUserIdAndShopId(userId, shopId));

        if (optionalProfile.isPresent()) {
            UserProfile profile = optionalProfile.get();
            profile.setAvailablePoints(profile.getAvailablePoints() + pointsToAdd);
            userProfileRepository.save(profile);  // 💾 Saves changes in transaction
        } else {
            throw new RuntimeException("User profile not found for userId " + userId + " and shopId " + shopId);
        }
    }
}