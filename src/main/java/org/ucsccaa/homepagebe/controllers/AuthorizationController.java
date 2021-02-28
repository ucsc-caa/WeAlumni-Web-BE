package org.ucsccaa.homepagebe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.ucsccaa.homepagebe.domains.User;
import org.ucsccaa.homepagebe.models.ServiceResponse;
import org.ucsccaa.homepagebe.models.Status;
import org.ucsccaa.homepagebe.services.AuthenticationService;

import java.util.Map;
import java.util.Optional;

@RestController
public class AuthorizationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("authenticate")
    public ServiceResponse<String> login(@RequestParam String username, @RequestParam String password) {
        Optional<User> result = authenticationService.authenticate(username, password);
        if (!result.isPresent()) {
            return new ServiceResponse<>(Status.NOT_FOUND, "User not found");
        }
        String token = authenticationService.generateToken(result.get());
        return new ServiceResponse<>(token);
    }
}
