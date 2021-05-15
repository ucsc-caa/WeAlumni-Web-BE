package org.ucsccaa.homepagebe.controllers;


import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ucsccaa.homepagebe.domains.User;
import org.ucsccaa.homepagebe.exceptions.ExceptionHandler;
import org.ucsccaa.homepagebe.exceptions.GenericServiceException;
import org.ucsccaa.homepagebe.models.GeneralResponse;
import org.ucsccaa.homepagebe.models.ServiceResponse;
import org.ucsccaa.homepagebe.models.Status;
import org.ucsccaa.homepagebe.services.DataProtection;
import org.ucsccaa.homepagebe.services.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api()
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService service;
    @Autowired
    private DataProtection dataProtection;

    @ApiOperation("Add new User")
    @PostMapping("/register")
    public ResponseEntity<GeneralResponse> addUser(@RequestParam String email, @RequestParam String password, HttpServletRequest req) throws URISyntaxException {
        try {
            User user = new User();
            user.setEmail(dataProtection.decrypt(email));
            user.setPassword(dataProtection.decrypt(password));
            System.out.println("password");
            user.setEmailVerfied(false);
            service.addUser(user);
            System.out.println("sss");
            return new ResponseEntity<>(new GeneralResponse(100, "continue"), HttpStatus.CONTINUE);
        } catch (Exception e) {
            System.out.println("sss");
            return new GenericServiceException(ExceptionHandler.USERS_EXISTS,"User exist").getExceptionHandler().getResponseEntity();
        }
    }

//    @ApiOperation("Update existed User by ID")
//    @PutMapping
//    public ServiceResponse<User> updateUser(@RequestParam Integer uid, @RequestParam String email, @RequestParam String password) {
//        User updatedUser = null;
//        User user = new User();
//        user.setUid(uid);
//        user.setEmail(email);
//        user.setPassword(password);
//        try {
//            updatedUser = service.updateUser(user);
//            if (updatedUser == null)
//                return new ServiceResponse<>(Status.NOT_FOUND, "USER NOT FOUND");
//        } catch (Exception e) {
//            return new ServiceResponse<>(Status.ERROR, e.getMessage());
//        }
//        return new ServiceResponse<>(updatedUser);
//    }

    @ApiOperation("Get User by ID")
    @GetMapping("/{Uid}")
    public ServiceResponse<User> getUserById(@PathVariable String email) {
        User user = null;
        try {
            user = service.getCensoredUserById(email);
            if (user == null)
                return new ServiceResponse<>(Status.NOT_FOUND, "ID NOT FOUND");
        } catch (Exception e) {
            return new ServiceResponse<>(Status.ERROR, e.getMessage());
        }
        return new ServiceResponse<>(user);
    }

}
