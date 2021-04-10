package org.ucsccaa.homepagebe.controllers;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
    @PostMapping("/register")
    public ServiceResponse<URI> addUser(@RequestParam String username, @RequestParam String name, @RequestParam String password, HttpServletRequest req) throws URISyntaxException {
        try {
            User user = new User();
            user.setUsername(username);
            user.setName(name);
            user.setPassword(password);
            Long id = service.addUser(user);
            return new ServiceResponse<>(new URI(req.getRequestURL() + "/" + id));
        } catch (Exception e) {
            return new ServiceResponse<>(Status.ERROR, e.getMessage());
        }
    }

    @ApiOperation("Update existed User by ID")
    @PutMapping
    public ServiceResponse<User> updateUser(@RequestParam Long id, @RequestParam String name, @RequestParam String password) {
        User updatedUser = null;
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setPassword(password);
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
            user = service.getCensoredUserById(id);
            if (user == null)
                return new ServiceResponse<>(Status.NOT_FOUND, "ID NOT FOUND");
        } catch (Exception e) {
            return new ServiceResponse<>(Status.ERROR, e.getMessage());
        }
        return new ServiceResponse<>(user);
    }
}
