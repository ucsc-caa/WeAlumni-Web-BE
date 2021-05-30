package org.ucsccaa.homepagebe.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ucsccaa.homepagebe.exceptions.customizedExceptions.VerificationCodeExpiredException;
import org.ucsccaa.homepagebe.services.DataProtection;
import org.ucsccaa.homepagebe.services.VerificationCodeService;
import org.ucsccaa.homepagebe.utils.CommonUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@Service
public class SimpleVerificationCodeServiceImpl implements VerificationCodeService {

    @Autowired
    private DataProtection dataProtection;

    @Override
    public String generateVerificationCode(Integer uid) {
        LocalDateTime currentTime = LocalDateTime.now().plusDays(1);
        String code = dataProtection.encrypt(currentTime + "|" + uid);
        return CommonUtils.boxingURLEncodingChar(code);
    }

    @Override
    public Integer getUid(String verificationCode) {
        verificationCode = CommonUtils.unboxingURLEncodingChar(verificationCode);
        verificationCode = dataProtection.decrypt(verificationCode);
        Integer uid = null;

        try {
            int delimiterPosition = verificationCode.lastIndexOf('|');
            if (delimiterPosition == -1) throw new RuntimeException();

            uid = Integer.parseInt(verificationCode.substring(delimiterPosition + 1));
            LocalDateTime timestamp = LocalDateTime.parse(verificationCode.substring(0, delimiterPosition));
            if (timestamp.isBefore(LocalDateTime.now())) throw new RuntimeException();
        } catch (DateTimeParseException e) {
            throw new VerificationCodeExpiredException("INVALID: broken timestamp");
        } catch (NumberFormatException e) {
            throw new VerificationCodeExpiredException("INVALID: broken uid");
        } catch (Exception e) {
            throw new VerificationCodeExpiredException(String.valueOf(uid));
        }

        return uid;
    }
}
