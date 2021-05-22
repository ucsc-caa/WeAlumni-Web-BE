package org.ucsccaa.homepagebe.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ucsccaa.homepagebe.domains.Member;
import org.ucsccaa.homepagebe.exceptions.GenericServiceException;
import org.ucsccaa.homepagebe.models.GeneralResponse;
import org.ucsccaa.homepagebe.services.MemberService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ApiOperation("Get user's membership info by UID")
    @GetMapping("/{uid}")
    public ResponseEntity<GeneralResponse> getMemberInfoByUID(@PathVariable Integer uid) {
        try {
            return new ResponseEntity<>(new GeneralResponse<>(memberService.getMemberInfo(uid)), HttpStatus.OK);
        } catch (GenericServiceException e) {
            return e.getExceptionHandler().getResponseEntity();
        }
    }

    @ApiOperation("Update Member info by uid")
    @PutMapping("/{uid}")
    public ResponseEntity<GeneralResponse> updateMemberInfo(@PathVariable("uid") Integer uid, @RequestBody Member member) {
        try {
            memberService.updateMember(uid, member);
            logger.info("Update Member info: uid-{}, memberId-{}, memberName-{}", uid, member.getMemberId(), member.getName());
            return new ResponseEntity<>(new GeneralResponse<>(), HttpStatus.OK);
        } catch (GenericServiceException e) {
            logger.error("Update Member info failed: e - {}", e.getMessage());
            return e.getExceptionHandler().getResponseEntity();
        }
    }

    @ApiOperation("Submit Member info for review")
    @PostMapping("/{uid}")
    public ResponseEntity<GeneralResponse> submitMemberInfoForReview(@PathVariable("uid") Integer uid, @RequestBody Member member) {
        try {
            memberService.submitMemberForReview(uid, member);
            logger.info("Submit Member info for review: uid-{}, memberId-{}, memberName-{}", uid, member.getMemberId(), member.getName());
            return new ResponseEntity<>(new GeneralResponse<>(), HttpStatus.OK);
        } catch (GenericServiceException e) {
            logger.error("Submit Member info failed: e - {}", e.getMessage());
            return e.getExceptionHandler().getResponseEntity();
        }
    }
}
