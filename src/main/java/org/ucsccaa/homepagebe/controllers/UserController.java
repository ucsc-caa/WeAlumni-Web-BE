package org.ucsccaa.homepagebe.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ucsccaa.homepagebe.domains.Member;
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

    @ApiOperation("Get user's membership info by UID")
    @GetMapping("/{uid}")
    public ResponseEntity<GeneralResponse> getUserById(@PathVariable Integer uid) {
        try {
            // TODO Get Member from MemberService
            Member member = new Member();
            return new ResponseEntity<>(new GeneralResponse<>(member), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionHandler.SERVER_ERROR.getResponseEntity();
        }
    }

}
