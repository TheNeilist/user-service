package com.neilism.user.service;

import com.neilism.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    public Optional<User> getUserBasicAuth(String authorization) {
        StringTokenizer tokenizer = new StringTokenizer(authorization);
        if (tokenizer.countTokens() < 2 || !tokenizer.nextToken().equalsIgnoreCase("Basic")) {
            return Optional.empty();
        }

        final String base64 = tokenizer.nextToken();
        final String credentials = new String(Base64.decode(base64.getBytes(StandardCharsets.UTF_8)));

        tokenizer = new StringTokenizer(credentials, ":");
        final String username = tokenizer.nextToken();
        final String password = tokenizer.nextToken();
        final User user = userService.findByUsername(username);
        if (user != null && User.PASSWORD_ENCODER.matches(password, user.getPassword())) {
            user.setAuthToken(generateAuthToken());
            //todo set auth expiration
            userService.saveUser(user);
            return Optional.of(user);
        }
        return Optional.empty();
    }

    public Optional<User> getUserTokenAuth(String token) {
        final User user = userService.findByAuthToken(token);
        //todo set auth expiration
        return Optional.ofNullable(user);
    }

    public String generateAuthToken() {
        return UUID.randomUUID().toString();
    }
}
