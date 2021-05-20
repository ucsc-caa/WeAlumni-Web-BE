package org.ucsccaa.homepagebe.services;

import static org.mockito.ArgumentMatchers.any;
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
import org.ucsccaa.homepagebe.domains.User;
import org.ucsccaa.homepagebe.repositories.MemberRepository;
import org.ucsccaa.homepagebe.repositories.UserRepository;
import org.ucsccaa.homepagebe.services.impl.SymmetricDataProtection;

@RunWith(MockitoJUnitRunner.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private SymmetricDataProtection symmetricDataProtection;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private MemberService memberService;

    private final Member.Address address = new Member.Address("street", "city", "country", "postal");
    private final Member.Degree degree = new Member.Degree("studentId", "program", 2021, "major1", "major2", "minor");
    private final Member.Career career = new Member.Career(true, "company", "position");
    private final Member.Status status = Member.Status.PENDING;
    private final Member expectedMember = new Member(1, 1,status, "name", true, "LocalDate.now()", "email", "phone", "wechat", 1,  address, degree, career, true);
    private final User user = new User("email", "password", 1, true);

    @Before
    public void configure() {
        when(memberRepository.save(eq(expectedMember))).thenReturn(expectedMember);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);
    }

    @Test
    public void testUpdateMember() {
        Member.Address address1 = new Member.Address(null, "test", "test", "test");
        Member.Degree degree1 = new Member.Degree("test", "test", 2021, "test", "test", "test");
        Member.Career career1 = new Member.Career(true, "company", "position");
        Member.Status status1 = Member.Status.PENDING;
        Member expectedMember1 = new Member(1, null, status1, "test", true, "LocalDate.now()", "test", "test", "test", 1,  address1, degree1, career1, true);
        when(memberRepository.existsById(any())).thenReturn(true);
        when(memberRepository.findById(any())).thenReturn(Optional.of(expectedMember));
        when(symmetricDataProtection.decrypt(eq(expectedMember1))).thenReturn(expectedMember1);
        memberService.updateMember(1, expectedMember1);
    }

    @Test(expected = RuntimeException.class)
    public void testUpdateMember_null() {
        memberService.updateMember(1, null);
    }

    @Test(expected = RuntimeException.class)
    public void testUpdateMember_notfound() {
        Member.Address address1 = new Member.Address("test", "test", "test", "test");
        Member.Degree degree1 = new Member.Degree("test", "test", 2021, "test", "test", "test");
        Member.Career career1 = new Member.Career(true, "company", "position");
        Member.Status status1 = Member.Status.PENDING;
        Member expectedMember1 = new Member(1, null, status1, "test", true, "LocalDate.now()", "test", "test", "test", 1,  address1, degree1, career1, true);
        when(memberRepository.existsById(any())).thenReturn(false);
        memberService.updateMember(1, expectedMember1);
    }

    @Test
    public void testUpdateEntireMember() {
        Member.Address address1 = new Member.Address("test", "test", "test", "test");
        Member.Degree degree1 = new Member.Degree("test", "test", 2021, "test", "test", "test");
        Member.Career career1 = new Member.Career(true, "company", "position");
        Member.Status status1 = Member.Status.PENDING;
        Member expectedMember1 = new Member(1, 1, status1, "test", true, "LocalDate.now()", "test", "test", "test", 1,  address1, degree1, career1, true);
        when(memberRepository.existsById(any())).thenReturn(true);
        memberService.updateEntireMember(1, expectedMember1);
    }

    @Test(expected = RuntimeException.class)
    public void testUpdateEntireMember_null() {
        memberService.updateEntireMember(1, null);
    }

    @Test(expected = RuntimeException.class)
    public void testUpdateEntireMember_notfound() {
        Member.Address address1 = new Member.Address("test", "test", "test", "test");
        Member.Degree degree1 = new Member.Degree("test", "test", 2021, "test", "test", "test");
        Member.Career career1 = new Member.Career(true, "company", "position");
        Member.Status status1 = Member.Status.PENDING;
        Member expectedMember1 = new Member(1, 1, status1, "test", true, "LocalDate.now()", "test", "test", "test", 1,  address1, degree1, career1, true);
        when(memberRepository.existsById(any())).thenReturn(false);
        memberService.updateEntireMember(1, expectedMember1);
    }
}