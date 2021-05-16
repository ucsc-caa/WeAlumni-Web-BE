package org.ucsccaa.homepagebe.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.ucsccaa.homepagebe.domains.Member;
import org.ucsccaa.homepagebe.domains.User;
import org.ucsccaa.homepagebe.exceptions.customizedExceptions.AuthenticationRequiredException;
import org.ucsccaa.homepagebe.exceptions.customizedExceptions.RequiredFieldIsNullException;
import org.ucsccaa.homepagebe.exceptions.customizedExceptions.UserExistException;
import org.ucsccaa.homepagebe.repositories.UserRepository;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepository repository;

    @Autowired
    private DataProtection dataProtection;

    public void register(String email, String password) {
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password))
            throw new RequiredFieldIsNullException("Request field is NULL or empty: email - " + email + ", password - " + password);

        if (repository.existsById(email))
            throw new UserExistException(email);

        User user = new User(email,dataProtection.encrypt(password),null,false);
        user = repository.save(user);
        logger.info("Registered new user: uid - {}", user.getUid());
        //TODO add member
        //TODO email service
    }

    public User updateUser(User user) {
        if (user == null)
            throw new RuntimeException("USER CANNOT BE NULL");
        if (user.getEmail() == null)
            throw new RuntimeException("USER ID CANNOT BE NULL");
        return repository.existsById(user.getEmail()) ? repository.save(user) : null;
    }

    public Member getUserByUid(String token, Integer uid) {
        if (StringUtils.isEmpty(token)) {
            throw new AuthenticationRequiredException("NO TOKEN PROVIDED");
        }
        //TODO token 解密
        //TODO uid 是否一致
        //TODO token是否过期
        //TODO ID 是否一直
        //TODO member info
        return new Member();
    }

}
