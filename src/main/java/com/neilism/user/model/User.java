
package com.neilism.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Transient;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {

    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    @JsonIgnore
    private Long id;
    @Column(name = "email")
    @Email(message = "Invalid email.")
    @NotEmpty(message = "Email is required.")
    private String email;
    @Column(name = "first_name")
    @NotEmpty(message = "First name is required.")
    private String first_name;
    @Column(name = "last_name")
    @NotEmpty(message = "Last name is required.")
    private String lastName;
    @Column(name = "username")
    @NotEmpty(message = "Username is required.")
    private String username;
    @Column(name = "password")
    @Length(min = 5, message = "Password must have at least 5 characters.")
    @NotEmpty(message = "Password is required.")
    @Transient
    @JsonIgnore
    private String password;
    @Column(name = "active")
    private boolean active;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
    @Column(name = "auth_token")
    @JsonIgnore
    private String authToken;
    @JsonIgnore
    @Column(name = "auth_expiration")
    private Date authExpiration;

    public User() {
    }

    public User(String email, String first_name, String lastName, String username, String password, boolean active, Set<Role> roles) {
        this.email = email;
        this.first_name = first_name;
        this.lastName = lastName;
        this.username = username;
        setPassword(password);
        this.active = active;
        this.roles = roles;
    }

    public User(Long id, String email, String first_name, String lastName, String username, String password, boolean active, Set<Role> roles, String authToken, Date authExpiration) {
        this.id = id;
        this.email = email;
        this.first_name = first_name;
        this.lastName = lastName;
        this.username = username;
        setPassword(password);
        this.active = active;
        this.roles = roles;
        this.authToken = authToken;
        this.authExpiration = authExpiration;
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

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
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
        this.password = PASSWORD_ENCODER.encode(password);
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