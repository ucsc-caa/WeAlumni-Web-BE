package org.ucsccaa.homepagebe.services.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.ucsccaa.homepagebe.domains.User;
import org.ucsccaa.homepagebe.repositories.UserRepository;
import org.ucsccaa.homepagebe.services.AuthenticationService;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Optional;


@Service
public class AuthenticationServiceImpl implements AuthenticationService {


    @Value("${secretKey}")
    private String secretKey;

    @Autowired
    private UserRepository userRepository;

    public String getEmailFromToken(String token) {
        if (token == null) {
            throw new RuntimeException("argument cannot be null");
        }
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    @Override
    public String encrypt(String password, byte[] salt) {
        if (password == null || salt == null) {
            throw new RuntimeException("argument cannot be null");
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            byte[] messageDigest = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] getSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return salt;
    }

    @Override
    public String generateToken(User user) {
        if (user == null) {
            throw new RuntimeException("argument cannot be null");
        }
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1h
//                .claim("authorizationLevel", level)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    @Override
    public Optional<User> authenticate(String email, String password) {
        if (email == null || password == null) {
            throw new RuntimeException("argument cannot be null");
        }
        Optional<User> user = userRepository.findByEmail(email);
        if (!user.isPresent()) {
            return Optional.empty();
        }
        byte[] salt = user.get().getSalt();
        String loginPassword = encrypt(password, salt);
        if (!loginPassword.equals(user.get().getPassword())) {
            return Optional.empty();
        }
        return user;
    }

    @Override
    public Boolean validateToken(String token) {
        String email;
        try {
            email = getEmailFromToken(token);
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        } catch (Exception e) {
            return false;
        }
        return (userRepository.findByEmail(email).isPresent());
    }
}
