package org.ucsccaa.homepagebe.services;

import java.util.Optional;

import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.ucsccaa.homepagebe.domains.User;
import org.ucsccaa.homepagebe.repositories.UserRepository;

import javax.annotation.PostConstruct;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;
    @Value("${PWD_SECRET_KEY}")
    private String secretKey;
    private byte[] salt;
    @PostConstruct
    public void setSalt(){
        salt = secretKey.getBytes();
    }
    @Autowired
    private AuthenticationService authenticationService;

    public String addUser(User user) {
        if (user == null)
            throw new RuntimeException("USER CANNOT BE NULL");
        user.setPassword(authenticationService.encrypt(user.getPassword(),salt));
        return repository.save(user).getEmail();
    }

    public User updateUser(User user) {
        if (user == null)
            throw new RuntimeException("USER CANNOT BE NULL");
        if (user.getEmail() == null)
            throw new RuntimeException("USER ID CANNOT BE NULL");
        return repository.existsById(user.getEmail()) ? repository.save(user) : null;
    }

    public User getUserById(String email) {
        if (email == null)
            throw new RuntimeException("ID CANNOT BE NULL");
        Optional<User> user = repository.findById(email);
        return user.orElse(null);
    }

    public User getCensoredUserById(String email) {
        User oUser = getUserById(email);
        User user = new User();
        user.setEmail(oUser.getEmail());
        user.setUid(oUser.getUid());
        user.setEmail(oUser.getEmail());
        return user;
    }
}
