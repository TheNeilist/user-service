package com.neilism.user.service;

import com.neilism.user.TestBuddy;
import com.neilism.user.model.User;
import com.neilism.user.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.times;

@RunWith(SpringRunner.class)
public class UserServiceTest {

    @TestConfiguration
    static class UserServiceImplTestContextConfiguration {
        @Bean
        public UserService userService() {
            return new UserServiceImpl();
        }
    }

    @Autowired
    private UserService userService;

    @MockBean
    private RoleService roleService;

    @MockBean
    private UserRepository userRepository;

    private final String USERNAME = "username";
    private final String PASSWORD = "password";

    @Test
    public void testSaveUser() {
        User user = TestBuddy.makeUser(USERNAME, PASSWORD);
        userService.saveUser(user);
        user.getRoles().forEach(role -> {
                    Mockito.when(roleService.findByRole(role.getRole())).thenReturn(role);
                    Mockito.verify(roleService, times(1)).findByRole(role.getRole());
                }
        );
        Mockito.verify(userRepository, times(1)).saveAndFlush(user);
    }
}
