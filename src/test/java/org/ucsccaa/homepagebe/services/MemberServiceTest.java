package org.ucsccaa.homepagebe.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.ucsccaa.homepagebe.domains.Member;
import org.ucsccaa.homepagebe.domains.User;
import org.ucsccaa.homepagebe.repositories.MemberRepository;

@RunWith(MockitoJUnitRunner.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private DataProtection dataProtection;

    @InjectMocks
    private MemberService memberService;

    private final Member.Address address = new Member.Address("street", "city", "country", "postal");
    private final Member.Degree degree = new Member.Degree("studentId", "program", 2021, "major1", "major2", "minor");
    private final Member.Career career = new Member.Career("就业", "company", "position");
    private final Member.Status status = Member.Status.PENDING;
    private final Member expectedMember = new Member(1, 1,status, "name", true, "LocalDate.now()", "email", "phone", "wechat", 1,  address, degree, career, true);
    private final User user = new User("email", "password", 1, true);
    private final Member.Address address1 = new Member.Address("test", "test", "test", "test");
    private final Member.Degree degree1 = new Member.Degree("test", "test", 2021, "test", "test", "test");
    private final Member.Career career1 = new Member.Career("就业", "company", "position");
    private final Member.Status status1 = Member.Status.PENDING;
    private final Member memberUpdate = new Member(1, null, status1, "test", true, "LocalDate.now()", "test", "test", "test", 1,  address1, degree1, career1, true);

    @Before
    public void configure() {
        when(memberRepository.save(any())).thenReturn(expectedMember);
    }

    @Test
    public void testRegister() {
        when(memberRepository.save(any())).thenReturn(expectedMember);
        memberService.register(1, "email");
    }

    @Test
    public void testUpdateMember() {
        when(memberRepository.existsById(any())).thenReturn(true);
        when(memberRepository.getOne(any())).thenReturn(expectedMember);
        when(dataProtection.decrypt(eq(memberUpdate))).thenReturn(memberUpdate);
        memberService.updateMember(1, memberUpdate);
    }

    @Test(expected = RuntimeException.class)
    public void testUpdateMember_notfound() {
        when(memberRepository.existsById(any())).thenReturn(false);
        memberService.updateMember(1, memberUpdate);
    }

    @Test
    public void submitMemberForReview() {
        when(dataProtection.decrypt(eq(expectedMember))).thenReturn(expectedMember);
        when(memberRepository.existsById(any())).thenReturn(true);
        memberService.submitMemberForReview(1, expectedMember);
    }

    @Test(expected = RuntimeException.class)
    public void submitMemberForReview_null() {
        memberService.submitMemberForReview(null, expectedMember);
    }

    @Test(expected = RuntimeException.class)
    public void submitMemberForReview_notfound() {
        when(dataProtection.decrypt(eq(expectedMember))).thenReturn(expectedMember);
        when(memberRepository.existsById(any())).thenReturn(false);
        memberService.submitMemberForReview(1, expectedMember);
    }

    @Test
    public void testGetMemberInfo() {
        when(memberRepository.findById(any())).thenReturn(Optional.of(expectedMember));
        memberService.getMemberInfo(1);
    }

    @Test(expected = RuntimeException.class)
    public void testGetMemberInfo_notfound() {
        when(memberRepository.findById(any())).thenReturn(null);
        memberService.getMemberInfo(1);
    }
}