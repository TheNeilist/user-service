package com.neilism.user.service;

import com.neilism.user.model.Role;
import com.neilism.user.model.User;
import com.neilism.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    public User findByAuthToken(String token) {
        return userRepository.findByAuthToken(token);
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        if (user.getRoles() != null) {
            //lookup all roles by name in case some foo passes in a bogus role
            Set<Role> roles = new HashSet<>();
            Iterator<Role> roleIterator = user.getRoles().iterator();
            while (roleIterator.hasNext()) {
                Role role = roleIterator.next();
                role = roleService.findByRole(role.getRole());
                if (role != null) {
                    roles.add(role);
                } else {
                    //todo: log unknown role
                }
            }
            user.setRoles(roles);
        }
        userRepository.saveAndFlush(user);
    }

    @Override
    public void deleteById(Long userId) {
        userRepository.delete(userId);
    }

    @Override
    @Transactional
    public void deleteByUsername(String username) {
        userRepository.deleteByUsername(username);
    }


}