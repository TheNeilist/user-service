package com.neilism.user.service;

import com.neilism.user.TestBuddy;
import com.neilism.user.model.User;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class AuthServiceTest {

    @TestConfiguration
    static class AuthServiceImplTestContextConfiguration {
        @Bean
        public AuthService authService() {
            return new AuthService();
        }
    }

    @Autowired
    private AuthService authService;

    @MockBean
    private UserService userService;

    private final String USERNAME = "username";
    private final String PASSWORD = "password";
    private final String TOKEN = "token";
    private User user;


    @Before
    public void setUp() {
        user = TestBuddy.makeUser(USERNAME, PASSWORD);
        user.setAuthToken(TOKEN);
    }

    @Test
    public void getBasicAuthSuccess() {
        Mockito.when(userService.findByUsername(user.getUsername()))
                .thenReturn(user);
        String authorization = USERNAME + ":" + PASSWORD;
        authorization = "Basic " + Base64.encodeBase64String(authorization.getBytes());
        Optional<User> user = authService.getUserBasicAuth(authorization);
        assertEquals(true, user.isPresent());
        assertEquals(user.get().getUsername(), USERNAME);
        assertNotNull(user.get().getAuthToken());
    }

    @Test
    public void getBasicAuthFail_WrongPassword() {
        Mockito.when(userService.findByUsername(USERNAME))
                .thenReturn(user);
        String authorization = USERNAME + ":incorrect";
        authorization = "Basic " + Base64.encodeBase64String(authorization.getBytes());
        Optional<User> user = authService.getUserBasicAuth(authorization);
        assertEquals(false, user.isPresent());
    }

    @Test
    public void getBasicAuthFail_UserNotFound() {
        Mockito.when(userService.findByUsername(USERNAME))
                .thenReturn(null);
        String authorization = USERNAME + ":" + PASSWORD;
        authorization = "Basic " + Base64.encodeBase64String(authorization.getBytes());
        Optional<User> user = authService.getUserBasicAuth(authorization);
        assertEquals(false, user.isPresent());
    }

    @Test
    public void getTokenAuthSuccess() {
        Mockito.when(userService.findByAuthToken(TOKEN))
                .thenReturn(user);
        Optional<User> user = authService.getUserTokenAuth(TOKEN);
        assertEquals(true, user.isPresent());
    }

    @Test
    public void getTokenAuthFail() {
        Mockito.when(userService.findByAuthToken(TOKEN))
                .thenReturn(null);
        Optional<User> user = authService.getUserTokenAuth(TOKEN);
        assertEquals(false, user.isPresent());
    }

}
