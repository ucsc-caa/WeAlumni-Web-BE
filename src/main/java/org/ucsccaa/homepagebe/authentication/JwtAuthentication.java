package org.ucsccaa.homepagebe.authentication;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.ucsccaa.homepagebe.domains.User;
import org.ucsccaa.homepagebe.exceptions.customizedExceptions.*;
import org.ucsccaa.homepagebe.repositories.UserAccessRepository;
import org.ucsccaa.homepagebe.repositories.UserRepository;
import org.ucsccaa.homepagebe.utils.AppConstants;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class JwtAuthentication implements Authentication {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${authentication.signingKey:UCSCCAASIGNINGKEY}")
    private String signingKey;
    @Value("#{new Long('${authentication.expiration.minutes:30}')}")
    private Long expirationInMinutes;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAccessRepository userAccessRepository;

    @Override
    public String generateToken(String identifier, String password) {
        if (StringUtils.isEmpty(identifier) || StringUtils.isEmpty(password))
            throw new BadRequestException("identifier or password is empty: identifier - " + identifier + ", password - " + password);

        Optional<User> user = userRepository.findById(identifier);
        if (!user.isPresent())
            throw new UserNotFoundException(identifier);

        if (!verifyUserPassword(user.get().getPassword(), password))
            throw new WrongPasswordException(identifier);

        return Jwts.builder()
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
                .setExpiration(Timestamp.valueOf(LocalDateTime.now().plusMinutes(expirationInMinutes)))
                .claim("uid", user.get().getUid())
                .claim("isAdmin", userAccessRepository.isAdminAccess(identifier))
                .signWith(SignatureAlgorithm.HS256, signingKey)
                .compact();
    }

    private boolean verifyUserPassword(String savedPassword, String givenPassword) {
        return savedPassword.equals(givenPassword);
    }

    @Override
    public Boolean validateToken(String token) {
        if (AppConstants.TEST_TOKEN.equals(token)) {
            logger.info("---- valid token: TEST USER TOKEN USED");
            return true;
        }

        try {
            token = token.replace("Bearer ", "");
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token);
            logger.info("---- valid token: uid = {}", claimsJws.getBody().get("uid"));
        } catch (ExpiredJwtException e) {
            logger.warn("---- invalid token: expired, token = {}", token);
            throw new AuthenticationExpiredException(token);
        } catch (NullPointerException | IllegalArgumentException e) {
            logger.warn("---- invalid token: null or empty");
            throw new AuthenticationRequiredException();
        } catch (Exception e) {
            logger.warn("---- invalid token: bad token, e = {}, token = {}", e.getMessage(), token);
            throw new AuthenticationHackedException(token);
        }
        return true;
    }

    @Override
    public <T> T getValue(String token, String key, Class<T> requiredType) {
        try {
            token = token.replace("Bearer ", "");
            Claims claims = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token).getBody();
            return claims.get(key, requiredType);
        } catch (Exception e) {
            return null;
        }
    }
}
