package com.sts.service;

import java.util.List;
import java.util.Optional;

import com.sts.entity.User;

public interface UserService 
{
	User createUser(User user);
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
}
