package com.neilism.user;

import com.neilism.user.model.Role;
import com.neilism.user.model.User;

import java.util.Arrays;
import java.util.HashSet;

public class TestBuddy {

    public static User makeUser(String username, String password) {
        return new User(
                "test@example.com",
                "firstname",
                "lastname",
                username,
                password,
                true,
                new HashSet<Role>(Arrays.asList(new Role(1L, "ROLE_USER"), new Role(1L, "ROLE_ADMIN")))
        );
    }
}
