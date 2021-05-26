package org.ucsccaa.homepagebe.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ucsccaa.homepagebe.exceptions.ExceptionHandler;
import org.ucsccaa.homepagebe.exceptions.GenericServiceException;
import org.ucsccaa.homepagebe.models.GeneralResponse;
import org.ucsccaa.homepagebe.services.UserService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @ApiOperation("Register new user")
    @PostMapping("/register")
    public ResponseEntity<GeneralResponse> register(@RequestParam String email, @RequestParam String password) {
        try {
            userService.register(email, password);
            return new ResponseEntity<>(new GeneralResponse<>(2101, "User Created", null), HttpStatus.OK);
        } catch (GenericServiceException e) {
            logger.error("Register new user failed: e - {}", e.getMessage());
            return e.getExceptionHandler().getResponseEntity();
        } catch (Exception e) {
            logger.error("Register new user failed: e - {}", e.getMessage());
            return ExceptionHandler.SERVER_ERROR.getResponseEntity();
        }
    }

    @ApiOperation("Verify the email address by the verification code")
    @GetMapping("/verify/{verificationCode}")
    public ResponseEntity<GeneralResponse> verify(@PathVariable String verificationCode) {
        try {
            userService.verifyRegistrationEmail(verificationCode);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (GenericServiceException e) {
            logger.error("Email registration verification failed: e - {}", e.getMessage());
            return e.getExceptionHandler().getResponseEntity();
        } catch (Exception e) {
            logger.error("Email registration verification failed: e - {}", e.getMessage());
            return ExceptionHandler.SERVER_ERROR.getResponseEntity();
        }
    }

    @ApiOperation("Send out email verification email to specific user")
    @PostMapping("/verify/send/{uid}")
    public void sendEmailVerificationEmail(@PathVariable Integer uid) {
        userService.sendRegistrationEmail(uid);
    }
}
