package org.ucsccaa.homepagebe.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.ucsccaa.homepagebe.domains.Member;
import org.ucsccaa.homepagebe.exceptions.customizedExceptions.UserNotFoundException;
import org.ucsccaa.homepagebe.services.MemberService;

@RunWith(SpringRunner.class)
public class MemberControllerTest {
    private MockMvc mockMvc;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private MemberController memberController;

    private final Member expectedMember = new Member(1, null, null,"test", true,
                                                     null, "test", "test", "test",
                                               1, null, null, null, true);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void configure() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
    }

    @Test
    public void updateMemberTest() throws Exception {
        String json = objectMapper.writeValueAsString(expectedMember);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .put("/member/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    public void updateMemberTest_exception() throws Exception {
        String json = objectMapper.writeValueAsString(expectedMember);
        doThrow(new UserNotFoundException("No Such Member")).when(memberService).updateMember(any(), any());
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .put("/member/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void updateEntireMemberTest() throws Exception {
        String json = objectMapper.writeValueAsString(expectedMember);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/member/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    public void updateEntireMemberTest_exception() throws Exception {
        doThrow(new UserNotFoundException("No Such Member")).when(memberService).updateEntireMember(any(), any());
        String json = objectMapper.writeValueAsString(expectedMember);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/member/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
