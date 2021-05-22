package org.ucsccaa.homepagebe.authentication;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.ucsccaa.homepagebe.domains.User;
import org.ucsccaa.homepagebe.exceptions.customizedExceptions.*;
import org.ucsccaa.homepagebe.repositories.UserAccessRepository;
import org.ucsccaa.homepagebe.repositories.UserRepository;
import org.ucsccaa.homepagebe.services.DataProtection;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JwtAuthenticationTests {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserAccessRepository userAccessRepository;
    @Mock
    private DataProtection dataProtection;
    @InjectMocks
    private JwtAuthentication jwtAuthentication;

    private final String email = "caa@ucsc.edu";
    private final String password = "pwd";
    private final Integer uid = 1;
    private final User user = new User(email, password, uid, true);

    @Before
    public void configure() {
        ReflectionTestUtils.setField(jwtAuthentication, "signingKey", "UCSCCAASIGNINGKEY");
        ReflectionTestUtils.setField(jwtAuthentication, "expirationInMinutes", 1L);
        when(userRepository.findById(eq(email))).thenReturn(Optional.of(user));
        when(userAccessRepository.isAdminAccess(eq(email))).thenReturn(true);
    }

    @Test
    public void testGenerateToken() {
        String token = jwtAuthentication.generateToken(email, password);
        assertNotNull(token);
    }

    @Test(expected = BadRequestException.class)
    public void testGenerateToken_emptyIdentifier() {
        jwtAuthentication.generateToken("", password);
    }

    @Test(expected = BadRequestException.class)
    public void testGenerateToken_emptyPassword() {
        jwtAuthentication.generateToken(email, null);
    }

    @Test(expected = UserNotFoundException.class)
    public void testGenerateToken_userNotFound() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());
        jwtAuthentication.generateToken("bad email", password);
    }

    @Test(expected = WrongPasswordException.class)
    public void testGenerateToken_wrongPassword() {
        jwtAuthentication.generateToken(email, "bad password");
    }

    @Test
    public void testValidateToken() {
        String token = jwtAuthentication.generateToken(email, password);
        assertTrue(jwtAuthentication.validateToken(token));
    }

    @Test(expected = AuthenticationExpiredException.class)
    public void testValidateToken_expiredToken() {
        ReflectionTestUtils.setField(jwtAuthentication, "expirationInMinutes", 0L);
        String token = jwtAuthentication.generateToken(email, password);
        jwtAuthentication.validateToken(token);
    }

    @Test(expected = AuthenticationRequiredException.class)
    public void testValidateToken_EmptyToken() {
        jwtAuthentication.validateToken(null);
    }

    @Test(expected = AuthenticationHackedException.class)
    public void testValidateToken_BadToken() {
        jwtAuthentication.validateToken("bad token");
    }

    @Test
    public void testGetValue() {
        String token = jwtAuthentication.generateToken(email, password);
        Integer actualUid = jwtAuthentication.getValue(token, "uid", Integer.class);
        assertEquals(uid, actualUid);
        Boolean isAdmin = jwtAuthentication.getValue(token, "isAdmin", Boolean.class);
        assertTrue(isAdmin);
    }

    @Test
    public void testGetValue_noSuchKey() {
        String token = jwtAuthentication.generateToken(email, password);
        String actual = jwtAuthentication.getValue(token, "not existing key", String.class);
        assertNull(actual);
    }
}
