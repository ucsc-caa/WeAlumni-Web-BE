//package org.ucsccaa.homepagebe.services;
//
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.ucsccaa.homepagebe.domains.Member;
//import org.ucsccaa.homepagebe.domains.User;
//import org.ucsccaa.homepagebe.domains.UserAccess;
//import org.ucsccaa.homepagebe.exceptions.customizedExceptions.RequiredFieldIsNullException;
//import org.ucsccaa.homepagebe.exceptions.customizedExceptions.UserExistException;
//import org.ucsccaa.homepagebe.exceptions.customizedExceptions.UserNotFoundException;
//import org.ucsccaa.homepagebe.models.LoginResponse;
//import org.ucsccaa.homepagebe.repositories.UserAccessRepository;
//import org.ucsccaa.homepagebe.repositories.UserRepository;
//
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
//public class UserServiceTest {
//    @Mock
//    private UserRepository userRepository;
//    @Mock
//    private UserAccessRepository userAccessRepository;
//    @Mock
//    private MemberService memberService;
//    @Mock
//    private EmailService emailService;
//    @InjectMocks
//    private UserService userService;
//
//    private final Member.Address address = new Member.Address("street", "city", "country", "postal");
//    private final Member.Degree degree = new Member.Degree("studentId", "program", 2021, "major1", "major2", "minor");
//    private final Member.Career career = new Member.Career(true, "company", "position");
//    private final Member.Status status = Member.Status.PENDING;
//    private final Member expectedMember = new Member(1, 1, status, "name", true, "LocalDate.now()", "email", "phone", "wechat", 1, address, degree, career, true);
//    private final User user = new User("email", "password", 1, false);
//    private final UserAccess userAccess = new UserAccess("email", false);
//
//    @Test
//    public void registerUserBasicTest() {
//        when(userRepository.existsById(user.getEmail())).thenReturn(false);
//        when(userRepository.getNextUid()).thenReturn(user.getUid());
//        when(userRepository.save(user)).thenReturn(user);
//        when(userAccessRepository.save(userAccess)).thenReturn(userAccess);
//        userService.register(user.getEmail(), user.getPassword());
//    }
//
//    @Test(expected = RequiredFieldIsNullException.class)
//    public void registerUserNullFieldTest() {
//        userService.register(null, null);
//    }
//
//    @Test(expected = UserExistException.class)
//    public void registerUserExitTest() {
//        when(userRepository.existsById(any())).thenReturn(true);
//        userService.register(user.getEmail(), user.getPassword());
//    }
//
//    @Test
//    public void getBasicInfoByEmailBasicTest() {
//        when(userRepository.findById(user.getEmail())).thenReturn(Optional.of(user));
//        when(memberService.getMemberInfo(user.getUid())).thenReturn(expectedMember);
//        LoginResponse.BasicInfo basicInfo = userService.getBasicInfoByEmail(user.getEmail());
//        Assert.assertEquals(expectedMember.getMemberId(), basicInfo.getMemberId());
//    }
//
//    @Test(expected = UserNotFoundException.class)
//    public void getBasicInfoByEmail() {
//        when(userRepository.findById(user.getEmail())).thenReturn(Optional.empty());
//        userService.getBasicInfoByEmail(user.getEmail());
//    }
//}
