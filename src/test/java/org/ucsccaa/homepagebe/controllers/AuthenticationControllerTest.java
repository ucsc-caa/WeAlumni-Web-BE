package org.ucsccaa.homepagebe.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.ucsccaa.homepagebe.authentication.Authentication;
import org.ucsccaa.homepagebe.domains.Member;
import org.ucsccaa.homepagebe.domains.User;
import org.ucsccaa.homepagebe.exceptions.customizedExceptions.UserNotFoundException;
import org.ucsccaa.homepagebe.models.LoginResponse;
import org.ucsccaa.homepagebe.services.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private Authentication authentication;

    @MockBean
    private UserService userService;

    @InjectMocks
    private AuthenticationController authenticationController;

    private final User expectedUser = new User("email", "password", 1, true);
    private final LoginResponse.BasicInfo basicInfo = new LoginResponse.BasicInfo(expectedUser.getUid(), 1, "name", expectedUser.getEmail(), Member.Status.PENDING, expectedUser.getEmailVerified());
    private final String token = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2MjE4OTQxOTgsImV4cCI6MTYyMTg5NTk5OCwidWlkIjoxLCJpc0FkbWluIjpmYWxzZX0.jkQJ8ft0_SNDwXK7lKltcBoeCdQx_dYejLkekXBwX80";

    @Before
    public void Before() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    public void loginTest() throws Exception {
        when(authentication.generateToken(any(), any())).thenReturn(token);
        when(userService.getBasicInfoByEmail(any())).thenReturn(basicInfo);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/authenticate")
                .param("email", "email")
                .param("password", "password")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload").exists());
    }

    @Test
    public void loginTest_exception() throws Exception {
        when(authentication.generateToken(any(), any())).thenThrow(new UserNotFoundException("email"));
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/authenticate")
                .param("email", "email")
                .param("password", "password")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User Not Found: user - email"));
    }
}
