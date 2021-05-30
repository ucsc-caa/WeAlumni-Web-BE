package org.ucsccaa.homepagebe.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.ucsccaa.homepagebe.domains.Member;
import org.ucsccaa.homepagebe.exceptions.customizedExceptions.UserNotFoundException;
import org.ucsccaa.homepagebe.models.EmailTemplate;
import org.ucsccaa.homepagebe.models.LoginResponse;
import org.ucsccaa.homepagebe.domains.User;
import org.ucsccaa.homepagebe.domains.UserAccess;
import org.ucsccaa.homepagebe.exceptions.customizedExceptions.RequiredFieldIsNullException;
import org.ucsccaa.homepagebe.exceptions.customizedExceptions.UserExistException;
import org.ucsccaa.homepagebe.repositories.UserAccessRepository;
import org.ucsccaa.homepagebe.repositories.UserRepository;
import org.ucsccaa.homepagebe.utils.EmailTemplateType;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAccessRepository userAccessRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private VerificationCodeService verificationCodeService;
    @Autowired
    private EmailService emailService;

    @Value("${email.sender.public}")
    private String publicSender;
    @Transactional(rollbackOn = Exception.class)
    public void register(String email, String password) {
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password))
            throw new RequiredFieldIsNullException("Request field is NULL or empty: email - " + email + ", password - " + password);

        if (userRepository.existsById(email))
            throw new UserExistException(email);

        User user = new User(email, password, userRepository.getNextUid(), false);
        user = userRepository.save(user);
        logger.info("Registered new user: uid - {}", user.getUid());

        userAccessRepository.save(new UserAccess(email, false));
        memberService.register(user.getUid(), email);

        //TODO email service
    }

    public LoginResponse.BasicInfo getBasicInfoByEmail(String email) {
        User user = userRepository.findById(email).orElse(null);
        if (user == null) throw new UserNotFoundException(email);

        Member member = memberService.getMemberInfo(user.getUid());

        return new LoginResponse.BasicInfo(
                        user.getUid(),
                        member.getMemberId(),
                        member.getName(),
                        user.getEmail(),
                        member.getStatus(),
                        user.getEmailVerified());
    }
    public void verifyRegistrationEmail(String verificationCode) {
        if (verificationCode == null)
            throw new RequiredFieldIsNullException("verification code");

        int uid = verificationCodeService.getUid(verificationCode);
        Optional<User> optionalUser = userRepository.findByUid(uid);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEmailVerified(true);
            userRepository.save(user);
            logger.info("Email verified: uid - {}, email - {}", uid, user.getEmail());
        } else {
            throw new UserNotFoundException(String.valueOf(uid));
        }
    }

    public void sendRegistrationEmail(Integer uid) {
        Optional<User> optionalUser = userRepository.findByUid(uid);
        if (!optionalUser.isPresent())
            throw new UserNotFoundException(String.valueOf(uid));
        User user = optionalUser.get();
        Member member = memberService.getMemberInfo(user.getUid());

        EmailTemplate registrationEmail = new EmailTemplate(EmailTemplateType.EMAIL_VERIFICATION);
        registrationEmail.setSender(publicSender);
        registrationEmail.setSenderName("CAA Membership Center");
        registrationEmail.setReceiver(user.getEmail());
        registrationEmail.setReceiverName(member.getName() == null ? "新会员" : member.getName());
        registrationEmail.setSubject("[UCSC-CAA] 中国校友会-普通会员注册邮箱确认");
        registrationEmail.addPlaceholder("{VERIFICATION_CODE}", verificationCodeService.generateVerificationCode(uid));
        registrationEmail.addPlaceholder("{HOUR}", "1");
        emailService.sendMail(registrationEmail);
    }
}
