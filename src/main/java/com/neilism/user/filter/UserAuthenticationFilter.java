package com.neilism.user.filter;

import com.neilism.user.model.User;
import com.neilism.user.model.security.UserAuthentication;
import com.neilism.user.model.security.UserContext;
import com.neilism.user.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public final class UserAuthenticationFilter extends GenericFilterBean {

    private static final String HEADER_TOKEN = "X-Auth-Token";
    private static final String HEADER_AUTHORIZATION = "Authorization";

    @Autowired
    private AuthService authService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        final String authorization = httpRequest.getHeader(HEADER_AUTHORIZATION);
        final String token = httpRequest.getHeader(HEADER_TOKEN);
        Optional<User> userOptional = Optional.empty();
        if (token != null) {
            userOptional = authService.getUserTokenAuth(token);
        } else if (authorization != null) {
            userOptional = authService.getUserBasicAuth(authorization);
            userOptional.ifPresent(user -> httpResponse.setHeader(HEADER_TOKEN, user.getAuthToken()));
        }

        if (!userOptional.isPresent()) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            SecurityContextHolder.getContext()
                    .setAuthentication(new UserAuthentication(new UserContext(userOptional.get()), true));
            chain.doFilter(request, response);
        }

    }

}
