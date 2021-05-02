package org.ucsccaa.homepagebe.services;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Example;
import org.ucsccaa.homepagebe.domains.Member;
import org.ucsccaa.homepagebe.repositories.MemberRepository;

@RunWith(MockitoJUnitRunner.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    private final Member expectedMember = new Member(1, 1, null, "test", true, LocalDate.now(), "test", "test", "test", 1, null, null, null, true);

    @Before
    public void configure() {
        when(memberRepository.save(eq(expectedMember))).thenReturn(expectedMember);
        when(memberRepository.findByEmail("test")).thenReturn(java.util.Optional.of(expectedMember));
    }

    public MemberService getMemberService() {
        return memberService;
    }

    @Test
    public void testAddMember() {
        Integer id = memberService.addMember(expectedMember);
        Assert.assertEquals(expectedMember.getMemberid(), id);
    }

    @Test(expected = RuntimeException.class)
    public void testAddMember_exception() {
        when(memberRepository.save(eq(expectedMember))).thenThrow(new RuntimeException());
        memberService.addMember(expectedMember);
    }

    @Test(expected = RuntimeException.class)
    public void testAddMember_null() {
        memberService.addMember(null);
    }

    @Test(expected = RuntimeException.class)
    public void testAddMember_exists() {
        when(memberRepository.exists(Example.of(expectedMember))).thenReturn(true);
        memberService.addMember(expectedMember);
    }

    @Test
    public void testUpdateMember() {
        when(memberRepository.existsById(1)).thenReturn(true);
        Integer id = memberService.updateMember(expectedMember);
        Assert.assertEquals(Optional.of(1), Optional.of(id));
    }

    @Test(expected = RuntimeException.class)
    public void testUpdateMember_invalidArgument() {
        memberService.updateMember(null);
    }

    @Test(expected = RuntimeException.class)
    public void testUpdateMember_exception() {
        when(memberRepository.existsById(1)).thenReturn(true);
        when(memberRepository.save(eq(expectedMember))).thenThrow(new RuntimeException());
        memberService.updateMember(expectedMember);
    }

    @Test(expected = RuntimeException.class)
    public void testUpdateMember_notfound() {
        when(memberRepository.existsById(1)).thenReturn(false);
        memberService.updateMember(expectedMember);
    }

    @Test
    public void testGetMemberByEmail() {
        Integer id = memberService.getMember("test").get().getMemberid();
        Assert.assertEquals(expectedMember.getMemberid(), id);
    }

    @Test(expected = RuntimeException.class)
    public void testGetMemberByEmail_invalidArgument() {
        memberService.getMember(null);
    }

    @Test(expected = RuntimeException.class)
    public void testGetMemberByEmail_exception() {
        when(memberRepository.findByEmail("test")).thenThrow(new RuntimeException());
        memberService.getMember("test");
    }

    @Test
    public void testDeleteMember() {
        when(memberRepository.existsByMemberid(1)).thenReturn(true);
        memberService.deleteMember(1);
        verify(memberRepository, times(1)).deleteByMemberid(1);
    }

    @Test(expected = RuntimeException.class)
    public void testDeleteMember_null() {
        memberService.deleteMember(null);
    }

    @Test
    public void testDeleteMember_notfound() {
        when(memberRepository.existsByMemberid(1)).thenReturn(false);
        Boolean result = memberService.deleteMember(1);
        Assert.assertEquals(false, result);
    }
}