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
import org.ucsccaa.homepagebe.services.mailSenderHandler;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

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
            return new ResponseEntity<>(new GeneralResponse(2101, "User Created", null), HttpStatus.OK);
        } catch (GenericServiceException e) {
            logger.error("Register new user failed: e - {}", e.getMessage());
            return e.getExceptionHandler().getResponseEntity();
        } catch (Exception e) {
            logger.error("Register new user failed: e - {}", e.getMessage());
            e.printStackTrace();
            return ExceptionHandler.SERVER_ERROR.getResponseEntity();
        }
    }

    @GetMapping("/verify/{verification_code}")
    public ResponseEntity<GeneralResponse> verify(@PathVariable("verification_code") String verificationCode) throws UnsupportedEncodingException, MessagingException {
        try {
            boolean result = userService.verifyRegister(verificationCode);
            if (result == true) {
                return new ResponseEntity<>(new GeneralResponse(2101, "User is verified thru email", null), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new GeneralResponse(2101, "User verified failed", result), HttpStatus.OK);
            }
        } catch (GenericServiceException e) {
            logger.error("Verified failed: e - {}", e.getMessage());
            return e.getExceptionHandler().getResponseEntity();
        } catch (Exception e) {
            e.printStackTrace();
            return ExceptionHandler.SERVER_ERROR.getResponseEntity();
        }
    }
}
