package org.ucsccaa.homepagebe.services;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.ucsccaa.homepagebe.domains.Member;
import org.ucsccaa.homepagebe.repositories.MemberRepository;

@Service
public class MemberService {
    @Autowired
    private  MemberRepository memberRepository;

    public Integer addMember(Member member) {
        if (member == null) {
            throw new RuntimeException("argument cannot be null");
        }
        if (memberRepository.exists(Example.of(member))) {
            throw new RuntimeException("member already exists");
        }
        return memberRepository.save(member).getMemberId();
    }

    public Integer updateMember(Member member) {
        if (member == null || member.getMemberId() == null) {
            throw new RuntimeException("argument cannot be null");
        }
        if(memberRepository.existsById(member.getMemberId())) {
            return memberRepository.save(member).getMemberId();
        } else {
            throw new RuntimeException("member does not exist");
        }
    }

    public Boolean deleteMember(Integer memberid) {
        if (memberid == null) {
            throw new RuntimeException("argument cannot be null");
        }
        if (memberRepository.existsByMemberId(memberid)) {
            memberRepository.deleteByMemberId(memberid);
            return true;
        }
        return false;
    }

    public Optional<Member> getMember(String email) {
        if(email == null) {
            throw new RuntimeException("argument cannot be null");
        }
        return memberRepository.findByEmail(email);
    }
}
