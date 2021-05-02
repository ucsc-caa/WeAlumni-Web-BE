package org.ucsccaa.homepagebe.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.ucsccaa.homepagebe.domains.Member;
import org.ucsccaa.homepagebe.models.ServiceResponse;
import org.ucsccaa.homepagebe.models.Status;
import org.ucsccaa.homepagebe.services.MemberService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api()
@RestController
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @ApiOperation("Add new Member")
    @PostMapping
    public ServiceResponse<URI> addMember(@RequestBody Member member, HttpServletRequest req) throws URISyntaxException {
        Integer memberId;
        System.out.println("this is a test");
        try {
            memberId = memberService.addMember(member);
            return new ServiceResponse<>(new URI(req.getRequestURL() + "/" + memberId));
        } catch (Exception e) {
            return new ServiceResponse<>(Status.ERROR, e.getMessage());
        }
    }

    @ApiOperation("Update existed Member by MemberID")
    @PutMapping
    public  ServiceResponse<URI> updateMember(@RequestBody Member member, HttpServletRequest req) throws URISyntaxException {
        Integer memberId;
        try {
            memberId = memberService.updateMember(member);
            return new ServiceResponse<>(new URI(req.getRequestURL() + "/" + memberId));
        } catch (Exception e) {
            return new ServiceResponse<>(Status.ERROR, e.getMessage());
        }
    }

    @ApiOperation("delete Member by ID")
    @DeleteMapping("/{id}")
    public ServiceResponse<Object> deleteMember(@PathVariable Integer memberId) {
        boolean delete;
        try {
            delete = memberService.deleteMember(memberId);
            if (!delete) {
                return new ServiceResponse<>(Status.NOT_FOUND, "ID NOT FOUND");
            }
        } catch (Exception e) {
            return new ServiceResponse<>(Status.ERROR, e.getMessage());
        }
        return new ServiceResponse<>();
    }

    @ApiOperation("get Member by email")
    @GetMapping("/email/{email}")
    public ServiceResponse<Member> getMember(@PathVariable("email") String email) {
        Optional<Member> member;
        try {
            member = memberService.getMember(email);
            if (!member.isPresent()) {
                return new ServiceResponse<>(Status.NOT_FOUND, "ID NOT FOUND");
            }
        } catch (Exception e) {
            return new ServiceResponse<>(Status.ERROR, e.getMessage());
        }
        return new ServiceResponse<Member>(member.get());
    }
}
