package org.ucsccaa.homepagebe.services;

public interface VerificationCodeService {
    String generateVerificationCode(Integer uid);
    Integer getUid(String verificationCode);
}
