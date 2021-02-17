package org.ucsccaa.homepagebe.controllers;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ucsccaa.homepagebe.domains.User;
import org.ucsccaa.homepagebe.models.ServiceResponse;
import org.ucsccaa.homepagebe.models.Status;
import org.ucsccaa.homepagebe.services.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api()
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService service;

    @ApiOperation("Add new User")
    @PostMapping
    public ServiceResponse<URI> addUser(@RequestBody User user, HttpServletRequest req) throws URISyntaxException {
        try {
            Long id = service.addUser(user);
            return new ServiceResponse<>(new URI(req.getRequestURL() + "/" + id));
        } catch (Exception e) {
            return new ServiceResponse<>(Status.ERROR, e.getMessage());
        }
    }

    @ApiOperation("Update existed User by ID")
    @PutMapping
    public ServiceResponse<User> updateUser(@RequestBody User user) {
        User updatedUser = null;
        try {
            updatedUser = service.updateUser(user);
            if (updatedUser == null)
                return new ServiceResponse<>(Status.NOT_FOUND, "USER NOT FOUND");
        } catch (Exception e) {
            return new ServiceResponse<>(Status.ERROR, e.getMessage());
        }
        return new ServiceResponse<>(updatedUser);
    }

    @ApiOperation("Get User by ID")
    @GetMapping("/{id}")
    public ServiceResponse<User> getUserById(@PathVariable Long id) {
        User user = null;
        try {
            user = service.getUserById(id);
            if (user == null)
                return new ServiceResponse<>(Status.NOT_FOUND, "ID NOT FOUND");
        } catch (Exception e) {
            return new ServiceResponse<>(Status.ERROR, e.getMessage());
        }
        return new ServiceResponse<>(user);
    }
}
