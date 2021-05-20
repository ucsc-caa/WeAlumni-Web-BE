package org.ucsccaa.homepagebe.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Provider;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ucsccaa.homepagebe.domains.Member;
import org.ucsccaa.homepagebe.exceptions.ExceptionHandler;
import org.ucsccaa.homepagebe.models.GeneralResponse;
import org.ucsccaa.homepagebe.services.MemberService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api()
@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ApiOperation("Update existed Member")
    @PutMapping("/{uid}")
    public ResponseEntity<GeneralResponse> updateMember(@PathVariable("uid") Integer uid, @RequestBody Member member) {
        try {
            memberService.updateMember(uid, member);
            return new ResponseEntity<>(new GeneralResponse(200, "Success", null), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Update user failed: e - {}", e.getMessage());
            return ExceptionHandler.BAD_REQUEST.setMessage(e.getMessage()).getResponseEntity();
        }
    }

    @ApiOperation("Update existed Member")
    @PostMapping("/{uid}")
    public ResponseEntity<GeneralResponse> updateEntireMember(@PathVariable("uid") Integer uid, @RequestBody Member member) {
        try {
            memberService.updateEntireMember(uid, member);
            return new ResponseEntity<>(new GeneralResponse(200, "Success", null), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Update user failed: e - {}", e.getMessage());
            return ExceptionHandler.BAD_REQUEST.setMessage(e.getMessage()).getResponseEntity();
        }
    }
}
