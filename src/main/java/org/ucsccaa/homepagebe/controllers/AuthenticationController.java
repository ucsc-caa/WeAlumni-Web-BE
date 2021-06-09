package org.ucsccaa.homepagebe.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ucsccaa.homepagebe.authentication.Authentication;
import org.ucsccaa.homepagebe.models.LoginResponse;
import org.ucsccaa.homepagebe.exceptions.GenericServiceException;
import org.ucsccaa.homepagebe.models.GeneralResponse;
import org.ucsccaa.homepagebe.services.UserService;

@RestController
public class AuthenticationController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Authentication authentication;
    @Autowired
    private UserService userService;

    @PostMapping("/authenticate")
    public ResponseEntity<GeneralResponse> login(@RequestParam String email, @RequestParam String password) {
        try {
            String token = authentication.generateToken(email, password);
            logger.info("Succeed to generate token: email - {}", email);

            LoginResponse.BasicInfo basicInfo = userService.getBasicInfoByEmail(email);
            LoginResponse loginResponse = new LoginResponse(token, basicInfo);
            return new ResponseEntity<>(new GeneralResponse<>(loginResponse), HttpStatus.OK);
        } catch (GenericServiceException e) {
            logger.warn("Failed to generate token: e - {}", e.getMessage());
            return e.getExceptionHandler().getResponseEntity();
        }
    }
}
