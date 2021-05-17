package org.ucsccaa.homepagebe.services;

import java.lang.reflect.Field;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.ucsccaa.homepagebe.domains.Member;
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
    private DataProtection dataProtection;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public Integer updateMember(Integer uid, Member member) {
        if (member == null || uid == null) {
            throw new RequiredFieldIsNullException("Request field is NULL or empty: uid - " + uid + ", Member - " + member);
        }
        Member member1;
        if (!memberRepository.findById(uid).isPresent()) {
            throw new UserNotFoundException("No Such Member");
        }
        member1 = memberRepository.findById(uid).get();

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
                            Object value = field1Value.getClass().equals(String.class) ? dataProtection.decrypt(field1Value.toString()) : field1Value;
                            try {
                                field1.set(member1, value);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }

                        field1.setAccessible(false);
                    }
                } else {
                    Object value = fieldValue.getClass().equals(String.class) ? dataProtection.decrypt(fieldValue.toString()) : fieldValue;
                    try {
                        field.set(member1, value);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            field.setAccessible(false);
        }
        logger.info("Updated member： uid - {}", uid);
        return memberRepository.save(member1).getMemberId();
    }

    public static <T> boolean isJDKClass(T t) {
        return t.getClass().getPackage().getName().startsWith("java");
    }
}
