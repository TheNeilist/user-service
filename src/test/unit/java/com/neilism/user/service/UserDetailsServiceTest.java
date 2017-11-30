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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
public class UserDetailsServiceTest {

    @TestConfiguration
    static class UserDetailsServiceImplTestContextConfiguration {
        @Bean
        public UserDetailsService userDetailsService() {
            return new UserDetailsServiceImpl();
        }
    }

    @Autowired
    private UserDetailsService userDetailsService;

    @MockBean
    private UserRepository userRepository;

    private final String USERNAME = "username";
    private final String PASSWORD = "password";

    @Test
    public void testLoadUserByUsername() {
        User user = TestBuddy.makeUser(USERNAME, PASSWORD);
        Mockito.when(userRepository.findByUsername(USERNAME)).thenReturn(user);
        UserDetails userDetails = userDetailsService.loadUserByUsername(USERNAME);
        assertEquals(USERNAME, userDetails.getUsername());
        assertTrue(User.PASSWORD_ENCODER.matches(PASSWORD, userDetails.getPassword()));
        SimpleGrantedAuthority authority = ((Collection<SimpleGrantedAuthority>) userDetails.getAuthorities()).iterator().next();
        List<String> authorities = Arrays.asList(authority.getAuthority().split(","));
        user.getRoles().forEach(role -> assertTrue(authorities.contains(role.getRole())));
    }

}
