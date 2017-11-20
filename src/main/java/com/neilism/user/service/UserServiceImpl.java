package com.neilism.user.service;

import com.neilism.user.model.Role;
import com.neilism.user.model.User;
import com.neilism.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {

    private static final String DEFAULT_ROLE = "USER";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleService roleService;

    @Override
    public User findById(Long userId) {
        return userRepository.findOne(userId);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void saveUser(User user) {

        if (user.getRoles() != null) {
            List<Long> roleIds = new ArrayList<>();
            user.getRoles().forEach(r -> roleIds.add(r.getId()));
            user.setRoles(new HashSet<Role>(roleService.findRolesByRoleIdList(roleIds)));
        } else {
            user.setRoles(new HashSet<Role>(Arrays.asList(roleService.findByRole(DEFAULT_ROLE))));
        }

        userRepository.save(user);
    }

    @Override
    public void deleteUserById(Long userId) {
        userRepository.delete(userId);
    }



}