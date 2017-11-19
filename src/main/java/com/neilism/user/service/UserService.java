package com.neilism.user.service;


import com.neilism.user.model.User;

import java.util.List;

public interface UserService {

    public User findById(Long userId);

    public List<User> findAll();

    public User findUserByEmail(String email);

    public void saveUser(User user);

    public void deleteUserById(Long userId);
}