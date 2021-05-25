package org.ucsccaa.homepagebe.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.ucsccaa.homepagebe.domains.User;
import org.ucsccaa.homepagebe.exceptions.customizedExceptions.RequiredFieldIsNullException;
import org.ucsccaa.homepagebe.services.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@RunWith(SpringRunner.class)
public class UserControllerTest {
    @Mock
    private UserService service;
    @InjectMocks
    private UserController userController;

    private final User expectedUser = new User("email", "password", 1, true);
    private final String errorMessage = "error message";
    protected MockMvc mockMvc;

    @Before
    public void Before() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void registerSuccessTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
                .param("email", expectedUser.getEmail())
                .param("password", expectedUser.getPassword()))
                .andExpect(ResultMatcher.matchAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.jsonPath("$.code").value(2101),
                        MockMvcResultMatchers.jsonPath("$.message").value("User Created")
                )).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void registerFailGenericServiceException() throws Exception {
        doThrow(new RequiredFieldIsNullException(errorMessage)).when(service).register(any(), any());
        mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
                .param("email", expectedUser.getEmail())
                .param("password", expectedUser.getPassword()))
                .andExpect(ResultMatcher.matchAll(
                        MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.code").value(4002),
                        MockMvcResultMatchers.jsonPath("$.message").value("Required Field is null: field - " + errorMessage)
                )).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void registerFailOtherException() throws Exception {
        doThrow(new RuntimeException(errorMessage)).when(service).register(any(), any());
        mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
                .param("email", expectedUser.getEmail())
                .param("password", expectedUser.getPassword()))
                .andExpect(ResultMatcher.matchAll(
                        MockMvcResultMatchers.status().is5xxServerError()
                ));
    }

}
