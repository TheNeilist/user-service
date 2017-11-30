package com.neilism.user.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neilism.user.IntegrationTestUtil;
import com.neilism.user.UserApplication;
import com.neilism.user.model.Role;
import com.neilism.user.model.User;
import com.neilism.user.model.command.UserCommand;
import com.neilism.user.repository.RoleRepository;
import com.neilism.user.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = UserApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase()
@TestPropertySource(locations = "classpath:application-integration-test.properties")
public class UserControllerIntTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    IntegrationTestUtil integrationTestUtil;

    @Value("${test.user.admin.authtoken}")
    private String authToken;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static Role roleAdmin;
    private static Role roleUser;

    @Before
    public void initAdminUserAndBasicAuth() throws Exception {
        if (roleAdmin != null) return;
        integrationTestUtil.initTestData();
        roleAdmin = roleRepository.findByRole("ROLE_ADMIN");
        roleUser = roleRepository.findByRole("ROLE_USER");
    }

    private UserCommand testUserCommand() {
        return new UserCommand(
                "test@example.com",
                "first",
                "last",
                "testuser" + UUID.randomUUID().toString(),
                "testpassword" + UUID.randomUUID().toString(),
                true,
                new HashSet<Role>(Arrays.asList(roleUser))
        );
    }

    @Test
    public void create() throws Exception {
        UserCommand newUser = testUserCommand();
        MvcResult result = mvc.perform(
                post("/user")
                        .header("X-Auth-Token", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        UserCommand savedUser = objectMapper.readValue(result.getResponse().getContentAsString(), UserCommand.class);
        assertNull(savedUser.getId());
        assertEquals(newUser.getEmail(), savedUser.getEmail());
        assertEquals(newUser.getFirstName(), savedUser.getFirstName());
        assertEquals(newUser.getLastName(), savedUser.getLastName());
        assertEquals(newUser.getUsername(), savedUser.getUsername());
        assertNull(savedUser.getPassword());
        assertEquals(newUser.isActive(), savedUser.isActive());
        assertEquals(1, savedUser.getRoles().size());
        assertEquals(newUser.getRoles().iterator().next().getId(), savedUser.getRoles().iterator().next().getId());
        assertNull(newUser.getAuthToken());
        assertNull(newUser.getAuthExpiration());
    }

    @Test
    public void update() throws Exception {
        UserCommand newUser = testUserCommand();
        userRepository.saveAndFlush(new User(newUser));

        User updateUser = userRepository.findByUsername(newUser.getUsername());
        assertNotNull(updateUser.getId());
        updateUser.setEmail("updated@example");
        updateUser.setFirstName("updatedfirst");
        updateUser.setLastName("updatedlast");
        updateUser.setRoles(new HashSet<Role>(Arrays.asList(roleAdmin)));
        updateUser.setActive(false);

        MvcResult result = mvc.perform(
                put("/user")
                        .header("X-Auth-Token", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUser)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        UserCommand updatedUser = objectMapper.readValue(result.getResponse().getContentAsString(), UserCommand.class);
        assertNull(updatedUser.getId());
        assertEquals(updateUser.getEmail(), updatedUser.getEmail());
        assertEquals(updateUser.getFirstName(), updatedUser.getFirstName());
        assertEquals(updateUser.getLastName(), updatedUser.getLastName());
        assertEquals(updateUser.getUsername(), updatedUser.getUsername());
        assertNull(updatedUser.getPassword());
        assertEquals(updateUser.isActive(), updatedUser.isActive());
        assertEquals(updateUser.getRoles().iterator().next().getId(), updatedUser.getRoles().iterator().next().getId());
        assertNull(updateUser.getAuthToken());
        assertNull(updateUser.getAuthExpiration());
    }

    @Test
    public void getUserByUsername() throws Exception {
        UserCommand newUser = testUserCommand();
        userRepository.saveAndFlush(new User(newUser));
        MvcResult result = mvc.perform(
                get("/user/" + newUser.getUsername())
                        .header("X-Auth-Token", authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        UserCommand foundUser = objectMapper.readValue(result.getResponse().getContentAsString(), UserCommand.class);
        assertNull(foundUser.getId());
        assertEquals(foundUser.getEmail(), newUser.getEmail());
        assertEquals(foundUser.getFirstName(), newUser.getFirstName());
        assertEquals(foundUser.getLastName(), newUser.getLastName());
        assertEquals(foundUser.getUsername(), newUser.getUsername());
        assertNull(foundUser.getPassword());
        assertEquals(foundUser.isActive(), newUser.isActive());
        assertEquals(foundUser.getRoles().iterator().next().getId(), newUser.getRoles().iterator().next().getId());
        assertNull(foundUser.getAuthToken());
        assertNull(foundUser.getAuthExpiration());
    }

    @Test
    public void getUsers() throws Exception {
        MvcResult result = mvc.perform(
                get("/user")
                        .header("X-Auth-Token", authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        List<UserCommand> users = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<UserCommand>>() {
        });
        assertNotNull(users);
        assertEquals(true, users.size() > 0);
    }

    @Test
    public void deleteUser() throws Exception {
        UserCommand newUser = testUserCommand();
        userRepository.saveAndFlush(new User(newUser));
        assertNotNull(userRepository.findByUsername(newUser.getUsername()));
        mvc.perform(
                delete("/user/" + newUser.getUsername())
                        .header("X-Auth-Token", authToken))
                .andExpect(status().isOk())
                .andReturn();
        assertNull(userRepository.findByUsername(newUser.getUsername()));
    }


}
