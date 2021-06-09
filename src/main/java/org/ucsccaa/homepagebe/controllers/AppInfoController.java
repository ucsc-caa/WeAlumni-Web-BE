package org.ucsccaa.homepagebe.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app-info")
public class AppInfoController {

    @GetMapping("/health")
    public String health() {
        return "Service is healthy";
    }

}
