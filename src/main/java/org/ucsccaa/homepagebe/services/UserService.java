package org.ucsccaa.homepagebe.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.ucsccaa.homepagebe.domains.LoginResponse;
import org.ucsccaa.homepagebe.domains.User;
import org.ucsccaa.homepagebe.domains.UserAccess;
import org.ucsccaa.homepagebe.exceptions.customizedExceptions.RequiredFieldIsNullException;
import org.ucsccaa.homepagebe.exceptions.customizedExceptions.UserExistException;
import org.ucsccaa.homepagebe.repositories.UserAccessRepository;
import org.ucsccaa.homepagebe.repositories.UserRepository;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAccessRepository userAccessRepository;

    public void register(String email, String password) {
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password))
            throw new RequiredFieldIsNullException("Request field is NULL or empty: email - " + email + ", password - " + password);

        if (userRepository.existsById(email))
            throw new UserExistException(email);

        User user = new User(email, password, null, false);
        user = userRepository.save(user);
        logger.info("Registered new user: uid - {}", user.getUid());
        userAccessRepository.save(new UserAccess(email, false));
        //TODO add member
        //TODO email service
    }

    public LoginResponse getLoginInfoByEmail(String email) {
        // TODO add actual logic
        return new LoginResponse();
    }
}
