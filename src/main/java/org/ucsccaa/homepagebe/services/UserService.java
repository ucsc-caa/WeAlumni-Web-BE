package org.ucsccaa.homepagebe.services;

import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.ucsccaa.homepagebe.domains.Member;
import org.ucsccaa.homepagebe.exceptions.customizedExceptions.UserNotFoundException;
import org.ucsccaa.homepagebe.models.LoginResponse;
import org.ucsccaa.homepagebe.domains.User;
import org.ucsccaa.homepagebe.domains.UserAccess;
import org.ucsccaa.homepagebe.exceptions.customizedExceptions.RequiredFieldIsNullException;
import org.ucsccaa.homepagebe.exceptions.customizedExceptions.UserExistException;
import org.ucsccaa.homepagebe.repositories.UserAccessRepository;
import org.ucsccaa.homepagebe.repositories.UserRepository;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAccessRepository userAccessRepository;
    @Autowired
    private MemberService memberService;

    // mail sender & verification code
    @Autowired
    private mailSenderHandler mailSenderHandler;
    @Autowired
    private verifyCodeService verifyCodeService;
    private DataProtection dataProtection;
    @Transactional(rollbackOn = Exception.class)
    public void register(String email, String password) throws UnsupportedEncodingException, MessagingException {
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
        mailSenderHandler.sendMail(email, "verify to register", "verify", user.getUid());
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

    public Boolean verifyRegister(String verificationCode) throws UnsupportedEncodingException, MessagingException {
        String code = dataProtection.decrypt(verificationCode);
        if (code == null) throw new RequiredFieldIsNullException("Request filed is Null or empty: verification code - " + code);
        if (verifyCodeService.verificateCode(code) == true) {
            return true;
        } else {
            int id_From_verificationCode = Integer.parseInt(code.substring(code.length() - 1));
            User user = userRepository.findByUid(id_From_verificationCode).get();
            if (user == null) {
                throw new UserNotFoundException("User Not Found by the uid");
            } else {
                mailSenderHandler.sendMail(user.getEmail(), "verify to register", "verify/", user.getUid());
            }
            return false;
        }
    }
}
