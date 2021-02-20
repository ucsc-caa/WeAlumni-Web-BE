package org.ucsccaa.homepagebe.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.ucsccaa.homepagebe.HomepageBeApplication;
import org.ucsccaa.homepagebe.services.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HomepageBeApplication.class)
public class UserControllerTest {
    protected MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private UserService service;
    @InjectMocks
    private UserController controller;
    
}
