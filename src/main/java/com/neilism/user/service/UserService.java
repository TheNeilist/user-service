package com.neilism.user.service;

import com.neilism.user.model.User;

import java.util.List;

public interface UserService {

    public User findById(Long userId);

    public List<User> findAll();

    public User findByEmail(String email);

    public User findByUsername(String username);

    public void saveUser(User user);

    public void deleteUserById(Long userId);

}