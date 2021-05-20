package org.ucsccaa.homepagebe.services;

import java.lang.reflect.Field;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ucsccaa.homepagebe.domains.Member;
import org.ucsccaa.homepagebe.domains.User;
import org.ucsccaa.homepagebe.exceptions.customizedExceptions.RequiredFieldIsNullException;
import org.ucsccaa.homepagebe.exceptions.customizedExceptions.UserNotFoundException;
import org.ucsccaa.homepagebe.repositories.MemberRepository;
import org.ucsccaa.homepagebe.repositories.UserRepository;
import org.ucsccaa.homepagebe.services.impl.SymmetricDataProtection;


@Service
public class MemberService {
    @Autowired
    private  MemberRepository memberRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SymmetricDataProtection symmetricDataProtection;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void updateMember(Integer uid, Member member) {
        if (member == null || uid == null) {
            throw new RequiredFieldIsNullException("Request field is NULL or empty: uid - " + uid + ", Member - " + member);
        }
        Member member1;
        if (!memberRepository.existsById(uid)) {
            throw new UserNotFoundException("No Such Member");
        }

        member1 = memberRepository.findById(uid).get();
        member = symmetricDataProtection.decrypt(member);

        if (member.getEmail() != null || member.getUid() != null) {
            User user = userRepository.findById(member1.getEmail()).get();
            if (member.getEmail() != null) {
                user.setEmail(member.getEmail());
            }
            if (member.getUid() != null) {
                user.setUid(member.getUid());
            }
            userRepository.save(user);
        }

        for (Field field : member.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object fieldValue;

            try {
                fieldValue = field.get(member);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            if (fieldValue != null) {
                if (!isJDKClass(fieldValue)) {
                    for (Field field1 : field.getClass().getDeclaredFields()) {
                        field1.setAccessible(true);
                        Object field1Value;
                        try {
                            field1Value = field1.get(member);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        if(field1Value != null) {
                            try {
                                field1.set(member1, field1Value);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }

                        field1.setAccessible(false);
                    }
                } else {
                    try {
                        field.set(member1, fieldValue);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            field.setAccessible(false);
        }
        member1.setStatus(Member.Status.PENDING);
        logger.info("Updated member： uid - {}", uid);
        memberRepository.save(member1);
    }

    public static <T> boolean isJDKClass(T t) {
        return t.getClass().getPackage().getName().startsWith("java");
    }

    public void updateEntireMember(Integer uid, Member member) {
        if (member == null || uid == null) {
            throw new RequiredFieldIsNullException("Request field is NULL or empty: uid - " + uid + ", Member - " + member);
        }
        if (!memberRepository.existsById(uid)) {
            throw new UserNotFoundException("No Such Member");
        }
        logger.info("Updated member： uid - {}", uid);
        memberRepository.save(member);
    }
}
