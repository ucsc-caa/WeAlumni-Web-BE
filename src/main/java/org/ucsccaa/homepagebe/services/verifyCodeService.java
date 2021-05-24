package org.ucsccaa.homepagebe.services;

import java.time.LocalDateTime;

public interface verifyCodeService {
    String createVerificationCode(int uid);
    boolean verificateCode(String code);
    LocalDateTime formatTime(LocalDateTime time);
    String formatTimeInStr(LocalDateTime time);
}
