package org.ucsccaa.homepagebe.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app-info")
public class AppInfoController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/health")
    public void health() {
        logger.info("App-Info: health check");
    }

}
