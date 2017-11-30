package com.neilism.user.controller;

import com.neilism.user.IntegrationTestUtil;
import com.neilism.user.UserApplication;
import com.neilism.user.repository.UserRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static junit.framework.TestCase.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = UserApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integration-test.properties")
public class AuthControllerIntTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    IntegrationTestUtil integrationTestUtil;

    @Value("${test.user.admin.username}")
    private String adminUsername;

    @Value("${test.user.admin.password}")
    private String adminPassword;

    @Test
    public void authFail() throws Exception {
        String authString = "fail:auth";
        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        authString = new String(authEncBytes);
        MvcResult result = mvc.perform(
                post("/auth")
                        .header("Authorization", "Basic " + authString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    public void authSucceed() throws Exception {
        integrationTestUtil.initTestData();
        String authString = adminUsername + ":" + adminPassword;
        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        authString = new String(authEncBytes);
        MvcResult result = mvc.perform(
                post("/auth")
                        .header("Authorization", "Basic " + authString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        String authToken = result.getResponse().getContentAsString();
        assertNotNull(authToken);
    }
}
