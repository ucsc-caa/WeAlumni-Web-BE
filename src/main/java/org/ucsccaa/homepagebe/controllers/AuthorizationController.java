package org.ucsccaa.homepagebe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ucsccaa.homepagebe.domains.User;
import org.ucsccaa.homepagebe.models.ServiceResponse;
import org.ucsccaa.homepagebe.models.Status;
import org.ucsccaa.homepagebe.services.AuthenticationService;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthorizationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("login")
    public ServiceResponse<String> login(@RequestBody Map<String, Object> params) {
        String email = (String) params.get("email");
        String password = (String) params.get("password");
        Optional<User> result = authenticationService.authenticate(email, password);
        if (!result.isPresent()) {
            return new ServiceResponse<>(Status.NOT_FOUND, "User not found");
        }
        String token = authenticationService.generateToken(result.get());
        return new ServiceResponse<>(token);
    }
}
