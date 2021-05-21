package org.ucsccaa.homepagebe.services;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ucsccaa.homepagebe.domains.Member;
import org.ucsccaa.homepagebe.exceptions.customizedExceptions.RequiredFieldIsNullException;
import org.ucsccaa.homepagebe.exceptions.customizedExceptions.ServerErrorException;
import org.ucsccaa.homepagebe.exceptions.customizedExceptions.UserNotFoundException;
import org.ucsccaa.homepagebe.repositories.MemberRepository;
import org.ucsccaa.homepagebe.services.impl.SymmetricDataProtection;
import org.ucsccaa.homepagebe.utils.Encryptable;

@Service
public class MemberService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private SymmetricDataProtection symmetricDataProtection;

    public void register(Integer uid, String email) {
        Member member = new Member();
        member.setUid(uid);
        member.setEmail(email);
        member.setStatus(Member.Status.DEACTIVE);
        member.setSearch(false);
        member.setBranch(0);
        memberRepository.save(member);
        logger.info("Registered new Member: uid - {}, email - {}", uid, email);
    }

    public void updateMember(Integer uid, Member member) {
        if (member == null || uid == null) {
            throw new RequiredFieldIsNullException("Requested field is NULL: uid - " + uid);
        }

        if (!memberRepository.existsById(uid)) {
            throw new UserNotFoundException("No Member Found: uid - " + uid);
        }

        logger.debug("Start update Member: uid - " + uid);
        Member savedMember = memberRepository.getOne(uid);
        member = symmetricDataProtection.decrypt(member);

        try {
            fillInDomain(savedMember, member);
            memberRepository.save(savedMember);
        } catch (Exception e) {
            throw new ServerErrorException("Failed to load the new Member info: uid - " + uid + ", e - " + e.getMessage());
        }
    }

    private <T> void fillInDomain(T saved, T given) throws Exception {
        logger.debug("Start fillInDomain()");
        for (Field field : given.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object givenFieldValue = field.get(given);

            if (givenFieldValue == null) continue;

            if (field.isAnnotationPresent(Encryptable.class)) {
                Object savedFieldValue = field.get(saved);
                if (savedFieldValue == null) {
                    savedFieldValue = field.getType().getDeclaredConstructor().newInstance();
                    field.set(saved, savedFieldValue);
                }
                fillInDomain(savedFieldValue, givenFieldValue);
            } else {
                field.set(saved, givenFieldValue);
            }
        }
        logger.debug("End fillInDomain()");
    }

    public void submitMemberForReview(Integer uid, Member member) {
        if (member == null || uid == null) {
            throw new RequiredFieldIsNullException("Requested field is NULL: uid - " + uid);
        }

        if (!memberRepository.existsById(uid)) {
            throw new UserNotFoundException("No Member Found: uid - " + uid);
        }

        member.setStatus(Member.Status.PENDING);
        memberRepository.save(member);
    }

    public Member getMemberInfo(Integer uid) {
        return memberRepository.findById(uid).orElse(null);
    }
}
