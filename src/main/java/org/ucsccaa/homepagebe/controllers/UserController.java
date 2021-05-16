package org.ucsccaa.homepagebe.controllers;



import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ucsccaa.homepagebe.domains.Member;
import org.ucsccaa.homepagebe.exceptions.GenericServiceException;
import org.ucsccaa.homepagebe.models.GeneralResponse;
import org.ucsccaa.homepagebe.services.DataProtection;
import org.ucsccaa.homepagebe.services.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api()
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private DataProtection dataProtection;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ApiOperation("Add new User")
    @PostMapping("/register")
    public ResponseEntity<GeneralResponse> register(@RequestParam String email, @RequestParam String password) {
        try {
            userService.register(email, password);
            return new ResponseEntity<>(new GeneralResponse(100, "continue"), HttpStatus.CONTINUE);
        } catch (GenericServiceException e) {
            logger.error("register failed: {}",e.getMessage());
            return e.getExceptionHandler().getResponseEntity();
        }
    }

    @ApiOperation("Get User by ID")
    @GetMapping("/{uid}")
    public ResponseEntity<GeneralResponse> getUserById(@PathVariable Integer uid,HttpServletRequest request) {
        String bear_token = request.getHeader("authorization");
        try {
            Member member = userService.getUserByUid(bear_token,uid);
            return new ResponseEntity<>(new GeneralResponse(200,member),HttpStatus.OK);
        } catch (GenericServiceException e) {
            return e.getExceptionHandler().getResponseEntity();
        }
    }

}
