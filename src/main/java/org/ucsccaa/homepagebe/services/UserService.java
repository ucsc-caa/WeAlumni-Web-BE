package org.ucsccaa.homepagebe.services;

import java.util.Optional;

import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.ucsccaa.homepagebe.domains.Member;
import org.ucsccaa.homepagebe.domains.User;
import org.ucsccaa.homepagebe.exceptions.customizedExceptions.AuthenticationRequiredException;
import org.ucsccaa.homepagebe.exceptions.customizedExceptions.BadRequestException;
import org.ucsccaa.homepagebe.exceptions.customizedExceptions.RequiredFieldIsNullException;
import org.ucsccaa.homepagebe.exceptions.customizedExceptions.UserExistException;
import org.ucsccaa.homepagebe.repositories.UserRepository;

import javax.annotation.PostConstruct;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private DataProtection dataProtection;

    public String addUser(User user) {
        if (user == null)
            throw new RuntimeException("USER CANNOT BE NULL");
        user.setPassword(dataProtection.encrypt(user.getPassword()));
        return repository.save(user).getEmail();
    }
    public void register(String email, String password) {
        if(StringUtils.isEmpty(email) || StringUtils.isEmpty(password)){
            throw new RequiredFieldIsNullException("Request field is NULL Email :"+email+" Password: "+password);
        }
        if (repository.existsById(email)){
            throw new UserExistException(email);
        }
        User user = new User(email,dataProtection.encrypt(password),null,false);
        repository.save(user);
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
