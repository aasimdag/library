package com.masparaga.library.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.masparaga.library.JsonHelper;
import com.masparaga.library.model.requests.AuthRequest;
import com.masparaga.library.model.requests.CreateUserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TestAuthApi {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;


    @Autowired
    public TestAuthApi(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void testLoginSuccess() throws Exception {
        AuthRequest request = new AuthRequest();
        String username = "user3@gmail.com";
        String password = "password123";
        request.setUsername(username);
        request.setPassword(password);

        MvcResult createResult = this.mockMvc
                .perform(post("/api/home/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonHelper.toJson(objectMapper, request)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();

        System.out.println(createResult.getResponse().getContentAsString());
    }

    @Test
    public void testLoginFail() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setUsername("user3@gmail.com");
        request.setPassword("wrongPassword");

        this.mockMvc
                .perform(post("/api/home/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonHelper.toJson(objectMapper, request)))
                .andExpect(status().isUnauthorized())
                .andExpect(header().doesNotExist(HttpHeaders.AUTHORIZATION))
                .andReturn();
    }

    @Test
    public void testRegisterSuccess() throws Exception {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("newUser"+System.currentTimeMillis()+"@gmail.com");
        request.setPassword("password123");
        MvcResult createResult = this.mockMvc
                .perform(post("/api/home/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonHelper.toJson(objectMapper, request)))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(createResult.getResponse().getContentAsString());
    }

    @Test
    public void testRegisterFail() throws Exception {
        CreateUserRequest badRequest = new CreateUserRequest();
        badRequest.setUsername("invalidUser");

        this.mockMvc
                .perform(post("/api/home/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonHelper.toJson(objectMapper, badRequest)))
                .andExpect(status().isBadRequest());
    }

}
