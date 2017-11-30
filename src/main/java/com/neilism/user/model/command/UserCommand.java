
package com.neilism.user.model.command;

import com.neilism.user.model.Role;

import java.util.Date;
import java.util.Set;

public class UserCommand {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean active;
    private Set<Role> roles;
    private String authToken;
    private Date authExpiration;

    public UserCommand() {
    }

    public UserCommand(String email, String firstName, String lastName, String username, String password, boolean active, Set<Role> roles) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.active = active;
        this.roles = roles;
    }

    public UserCommand(Long id, String email, String firstName, String lastName, String username, String password, boolean active, Set<Role> roles, String authToken, Date authExpiration) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.active = active;
        this.roles = roles;
        this.authToken = authToken;
        this.authExpiration = authExpiration;
    }

    public UserCommand(Long id, String email, String firstName, String lastName, String username, String password, boolean active, Set<Role> roles) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.active = active;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Date getAuthExpiration() {
        return authExpiration;
    }

    public void setAuthExpiration(Date authExpiration) {
        this.authExpiration = authExpiration;
    }
}