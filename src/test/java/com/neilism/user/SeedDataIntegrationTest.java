package com.neilism.user;

import com.neilism.user.model.Role;
import com.neilism.user.model.User;
import com.neilism.user.repository.RoleRepository;
import com.neilism.user.repository.UserRepository;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashSet;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SeedDataIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Ignore
    @Test
    public void loadSeedData() {

        userRepository.deleteAll();
        roleRepository.deleteAll();

        Role roleAdmin = new Role("ROLE_ADMIN");
        Role roleUser = new Role("ROLE_USER");

        User userAdmin = new User("admin@example.com", "Brian", "Kelly", "admin", "password", true, new HashSet(Arrays.asList(roleAdmin)));
        userRepository.save(userAdmin);
        User userUser = new User("user@example.com", "Brandon", "Wimbush", "user", "password", true, new HashSet(Arrays.asList(roleUser)));
        userRepository.save(userUser);
    }
}
