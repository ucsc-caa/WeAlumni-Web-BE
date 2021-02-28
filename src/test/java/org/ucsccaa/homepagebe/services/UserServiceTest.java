package org.ucsccaa.homepagebe.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import org.ucsccaa.homepagebe.domains.User;
import org.ucsccaa.homepagebe.repositories.UserRepository;

@RunWith(SpringRunner.class)
public class UserServiceTest {
    @Mock
    private UserRepository repository;
    @Mock
    private AuthenticationService authenticationService;
    @InjectMocks
    private UserService userService;
    private final User expectedUser = new User((long)1, "name", "password", "email", null);

    @Test
    public void addUserTest() {
        when(repository.save(any())).thenReturn(expectedUser);
        when(authenticationService.getSalt()).thenReturn(null);
        when(authenticationService.encrypt(any(),any())).thenReturn(null);
        Long id = userService.addUser(expectedUser);
        assertEquals(expectedUser.getId(), id);
    }

    @Test(expected = RuntimeException.class)
    public void addUserNullTest() {
        userService.addUser(null);
    }

    @Test
    public void updateUserTest() {
        when(repository.existsById(anyLong())).thenReturn(true);
        when(repository.save(any())).thenReturn(expectedUser);
        User user = userService.updateUser(expectedUser);
        assertNotNull(user);
        assertEquals(expectedUser.getId(), user.getId());
    }

    @Test(expected = RuntimeException.class)
    public void updateUserNullTest() { userService.updateUser(null); }

    @Test(expected = RuntimeException.class)
    public void updateUserIdNullTest() {
        expectedUser.setId(null);
        userService.updateUser(expectedUser);
    }

    @Test
    public void getUserTest() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(expectedUser));
        User user = userService.getUserById(expectedUser.getId());
        assertEquals(expectedUser.getId(), user.getId());
    }

    @Test(expected = RuntimeException.class)
    public void getUserIdNullTest() {
        userService.getUserById(null);
    }
}
