package org.ucsccaa.homepagebe.services.impl;

import org.springframework.stereotype.Service;
import org.ucsccaa.homepagebe.services.verifyCodeService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class verifyCodeServiceImpl implements verifyCodeService {
    private String verificationCode;
    @Override
    public String createVerificationCode(int uid) {
        //get time and generate it to verification time
        LocalDateTime currentTime = LocalDateTime.now();
        currentTime = currentTime.plusDays(1);

        //convert current time to String and generate to verification code
        verificationCode = formatTimeInStr(currentTime) + "|" + uid;
        return verificationCode;
    }

    @Override
    public boolean verificateCode(String code) {
        //get the uid from code
        int id_From_verificationCode = Integer.parseInt(code.substring(code.length() - 1));

        //get the verification time in String
        String timeStrFromCode = code.substring(0, code.length() - 2);

        //parse the verification time string to localDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime codeTime = LocalDateTime.parse(timeStrFromCode, formatter);

        //get the current time in the same format as verification time
        LocalDateTime currentTime = LocalDateTime.now();
        currentTime = formatTime(currentTime);

        //verify the verficationCode by times
        return (currentTime.isBefore(codeTime) || currentTime.isEqual(codeTime));
    }

    @Override
    public LocalDateTime formatTime(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String timeStr = time.format(formatter);
        LocalDateTime formattedTime = LocalDateTime.parse(timeStr, formatter);
        return formattedTime;
    }

    @Override
    public String formatTimeInStr(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return time.format(formatter);

    }
}
