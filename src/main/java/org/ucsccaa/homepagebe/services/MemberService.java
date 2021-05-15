package org.ucsccaa.homepagebe.services;

import java.lang.reflect.Field;
import java.util.*;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.ucsccaa.homepagebe.domains.Member;
import org.ucsccaa.homepagebe.repositories.MemberRepository;
import org.ucsccaa.homepagebe.repositories.UserRepository;
import org.ucsccaa.homepagebe.services.impl.SymmetricProtection;

@Service
public class MemberService {
    @Autowired
    private  MemberRepository memberRepository;
    @Autowired
    private UserRepository userRepository;
    private SymmetricProtection symmetricProtection;

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

    public Integer updateMember(Integer uid, Member member) throws IllegalAccessException {
        if (member == null || uid == null) {
            throw new RuntimeException("argument cannot be null");
        }
        if (!memberRepository.existsByMember_uid(uid)) {
            throw new RuntimeException("member does not exist");
        }
        Member member1 = memberRepository.findByMember_uid(uid).get();
        BeanUtils.copyProperties(member, member1);

        for (Field field : member.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object fieldValue = field.get(member);
            Class<?> fieldType = field.getType();
            if (fieldValue != null) {
                String decrypt = symmetricProtection.decrypt((String) fieldValue);

                field.set(member1, fieldType.cast(decrypt));
            }
            field.setAccessible(false);
        }

        return memberRepository.save(member1).getMemberId();
    }
    public Boolean deleteMember(Integer memberId) {
        if (memberId == null) {
            throw new RuntimeException("argument cannot be null");
        }
        if (memberRepository.existsByMemberId(memberId)) {
            memberRepository.deleteByMemberId(memberId);
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
