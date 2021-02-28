package org.ucsccaa.homepagebe.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.ucsccaa.homepagebe.HomepageBeApplication;
import org.ucsccaa.homepagebe.domains.User;
import org.ucsccaa.homepagebe.services.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HomepageBeApplication.class)
public class UserControllerTest {
    protected MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private UserService service;
    @InjectMocks
    private UserController controller;
    private final User expectedUser = new User((long)1, "name", "password", "email", null);
    private final String token = "test_token";

    @Before
    public void Before() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void addUserTest() throws Exception {
        when(service.addUser(any())).thenReturn(expectedUser.getId());
        String json = objectMapper.writeValueAsString(expectedUser);
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload")
                        .value("http://localhost/users/" + expectedUser.getId().toString()));
    }

    @Test
    public void addUserNullTest() throws Exception {
        when(service.addUser(null)).thenThrow(RuntimeException.class);
        String json = objectMapper.writeValueAsString(null);
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void addUserExceptionTest() throws Exception {
        when(service.addUser(any())).thenThrow(RuntimeException.class);
        String json = objectMapper.writeValueAsString(expectedUser);
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("ERROR"));
    }

    @Test
    public void updateUserTest() throws Exception {
        String json = objectMapper.writeValueAsString(expectedUser);
        when(service.updateUser(any())).thenReturn(expectedUser);
        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.id").value(expectedUser.getId()));
    }

    @Test
    public void updateUserNotFoundTest() throws Exception {
        String json = objectMapper.writeValueAsString(expectedUser);
        when(service.updateUser(any())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("NOT_FOUND"));
    }

    @Test
    public void updateUserExceptionTest() throws Exception {
        String json = objectMapper.writeValueAsString(expectedUser);
        when(service.updateUser(any())).thenThrow(RuntimeException.class);
        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("ERROR"));
    }

    @Test
    public void getUserByIdTest() throws Exception {
        when(service.getUserById(anyLong())).thenReturn(expectedUser);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/" + expectedUser.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.id").value(expectedUser.getId()));
    }

    @Test
    public void getUserByIdNotFoundTest() throws Exception {
        when(service.getUserById(anyLong())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("NOT_FOUND"));
    }

    @Test
    public void getUserByIdExceptionTest() throws Exception {
        when(service.getUserById(anyLong())).thenThrow(RuntimeException.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("ERROR"));
    }

}
