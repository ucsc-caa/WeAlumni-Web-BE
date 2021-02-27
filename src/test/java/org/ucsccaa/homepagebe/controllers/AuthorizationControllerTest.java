package org.ucsccaa.homepagebe.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.ucsccaa.homepagebe.domains.User;
import org.ucsccaa.homepagebe.services.AuthenticationService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(AuthorizationController.class)
public class AuthorizationControllerTest {
    @Autowired
    protected MockMvc mockMvc;
    @MockBean
    private AuthenticationService authenticationService;
    @InjectMocks
    private UserController controller;
    private final User expectedUser = new User((long)1, "name", "password", "email", null);
    private final String token = "test_token";

    @Test
    public void authenticateSuccessTest() throws Exception {
        when(authenticationService.authenticate(any(), any())).thenReturn(java.util.Optional.of(expectedUser));
        when(authenticationService.generateToken(any())).thenReturn("test_token");
        mockMvc.perform(MockMvcRequestBuilders.post("/authenticate")
                .param("username", "username")
                .param("password", "password")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload").exists());
    }

    @Test
    public void authenticateUserNotExistsTest() throws Exception {
        when(authenticationService.authenticate(any(), any())).thenReturn(java.util.Optional.ofNullable(null));
        when(authenticationService.generateToken(any())).thenReturn("test_token");
        mockMvc.perform(MockMvcRequestBuilders.post("/authenticate")
                .param("username", "username")
                .param("password", "password")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("NOT_FOUND"));
    }
}
