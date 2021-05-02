package org.ucsccaa.homepagebe.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import org.ucsccaa.homepagebe.services.MemberService;

@RunWith(SpringRunner.class)
public class MemberControllerTest {
    private MockMvc mockMvc;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private MemberController memberController;

    private final Member expectedMember = new Member(1, 1, null, "test", true,
                                                     LocalDate.now(), "test", "test", "test",
                                               1, null, null, null, true);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void configure() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
        when(memberService.addMember(expectedMember)).thenReturn(expectedMember.getMemberid());
    }

    @Test
    public void addMemberTest() throws Exception {
        String json = objectMapper.writeValueAsString(expectedMember);
        System.out.println("this is a test" + json);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload")
                        .value("/members/" + expectedMember.getMemberid()));
    }

    @Test
    public void addMemberTest_lackOfParam() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/members");

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void updateMemberTest() throws Exception {
        String json = objectMapper.writeValueAsString(expectedMember);
        when(memberService.updateMember(any())).thenReturn(expectedMember.getMemberid());
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .put("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.memberid").value(expectedMember.getMemberid()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.uid").value(expectedMember.getUid()));
    }

    @Test
    public void updateMemberTest_NotFound() throws Exception {
        String json = objectMapper.writeValueAsString(expectedMember);
        when(memberService.updateMember(any())).thenThrow(new RuntimeException("member does not exist"));
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .put("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void getMemberTest() throws Exception {
        when(memberService.getMember("test")).thenReturn(Optional.of(expectedMember));
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .get("/members/email/test");

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.uid").value(expectedMember.getUid()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.memberid").value(expectedMember.getMemberid()));
    }

    @Test
    public void getMemberTest_NotFound() throws Exception {
        when(memberService.getMember("test")).thenReturn(Optional.empty());
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .get("/members/id/test");

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());;
    }

    @Test
    public void deleteMemberTest() throws Exception {
        when(memberService.deleteMember(1)).thenReturn(true);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .delete("/members/1");

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    public void deleteMemberTest_NotFound() throws Exception {
        when(memberService.deleteMember(1)).thenReturn(false);
        mockMvc.perform(MockMvcRequestBuilders.delete("/members/1"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());;
    }
}
