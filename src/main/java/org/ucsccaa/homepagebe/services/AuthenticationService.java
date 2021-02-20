package org.ucsccaa.homepagebe.services;

import org.ucsccaa.homepagebe.domains.User;

import java.util.Optional;

public interface AuthenticationService {
    Boolean validateToken(String token);

    String generateToken(User user);

    String encrypt(String password, byte[] salt);

    byte[] getSalt();

    Optional<User> authenticate(String email, String password);
}
