package com.neilism.user.filter;

import com.neilism.user.TestBuddy;
import com.neilism.user.model.User;
import com.neilism.user.service.AuthService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@RunWith(SpringRunner.class)
public class UserAuthenticationFilterTest {

    @TestConfiguration
    static class UserAuthenticationFilterTestContextConfiguration {
        @Bean
        public UserAuthenticationFilter userAuthenticationFilter() {
            return new UserAuthenticationFilter();
        }
    }

    @Autowired
    private UserAuthenticationFilter userAuthenticationFilter;

    @MockBean
    private AuthService authService;

    private static final String HEADER_TOKEN = "X-Auth-Token";
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String TOKEN = "token";
    private static final String AUTHORIZATION = "authorization";

    private User user;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @Before
    public void setup() {
        user = TestBuddy.makeUser("username", "password");
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
        filterChain = Mockito.mock(FilterChain.class);
    }

    @Test
    public void doFilterTokenSuccess() throws Exception {
        Mockito.when(request.getHeader(HEADER_AUTHORIZATION)).thenReturn(null);
        Mockito.when(request.getHeader(HEADER_TOKEN)).thenReturn(TOKEN);
        Mockito.when(authService.getUserTokenAuth(TOKEN)).thenReturn(Optional.of(user));
        userAuthenticationFilter.doFilter(request, response, filterChain);
        Mockito.verify(authService, times(1)).getUserTokenAuth(TOKEN);
        Mockito.verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    public void doFilterTokenFail() throws Exception {
        Mockito.when(request.getHeader(HEADER_AUTHORIZATION)).thenReturn(null);
        Mockito.when(request.getHeader(HEADER_TOKEN)).thenReturn(TOKEN);
        Mockito.when(authService.getUserTokenAuth(TOKEN)).thenReturn(Optional.empty());
        userAuthenticationFilter.doFilter(request, response, filterChain);
        Mockito.verify(authService, times(1)).getUserTokenAuth(TOKEN);
        Mockito.verify(response, times(1)).sendError(HttpServletResponse.SC_UNAUTHORIZED);
        Mockito.verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    public void doFilterAuthorizationSuccess() throws Exception {
        Mockito.when(request.getHeader(HEADER_AUTHORIZATION)).thenReturn(AUTHORIZATION);
        Mockito.when(request.getHeader(HEADER_TOKEN)).thenReturn(null);
        Mockito.when(authService.getUserBasicAuth(AUTHORIZATION)).thenReturn(Optional.of(user));
        userAuthenticationFilter.doFilter(request, response, filterChain);
        Mockito.verify(authService, times(1)).getUserBasicAuth(AUTHORIZATION);
        Mockito.verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    public void doFilterAutorizationFail() throws Exception {
        Mockito.when(request.getHeader(HEADER_AUTHORIZATION)).thenReturn(AUTHORIZATION);
        Mockito.when(request.getHeader(HEADER_TOKEN)).thenReturn(null);
        Mockito.when(authService.getUserBasicAuth(AUTHORIZATION)).thenReturn(Optional.empty());
        userAuthenticationFilter.doFilter(request, response, filterChain);
        Mockito.verify(authService, times(1)).getUserBasicAuth(AUTHORIZATION);
        Mockito.verify(response, times(1)).sendError(HttpServletResponse.SC_UNAUTHORIZED);
        Mockito.verify(filterChain, never()).doFilter(request, response);
    }
}
