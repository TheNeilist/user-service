package com.neilism.user;

import com.neilism.user.model.Role;
import com.neilism.user.model.User;
import com.neilism.user.repository.RoleRepository;
import com.neilism.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;

@Component
public class IntegrationTestUtil {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Value("${test.user.admin.username}")
    private String adminUsername;

    @Value("${test.user.admin.password}")
    private String adminPassword;

    @Value("${test.user.admin.authtoken}")
    private String authToken;

    public void initTestData() {

        userRepository.deleteAll();
        roleRepository.deleteAll();

        Role roleAdmin = new Role("ROLE_ADMIN");
        roleRepository.save(roleAdmin);
        Role roleUser = new Role("ROLE_USER");
        roleRepository.save(roleUser);

        User userAdmin = new User("admin@example.com", "Brian", "Kelly", adminUsername, adminPassword, true, new HashSet(Arrays.asList(roleAdmin)));
        userAdmin.setAuthToken(authToken);
        userRepository.save(userAdmin);

    }

    public User getAdminUser() {
        return userRepository.findByUsername(adminUsername);
    }
}
