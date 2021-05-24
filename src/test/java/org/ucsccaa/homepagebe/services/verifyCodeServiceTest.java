package org.ucsccaa.homepagebe.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.ucsccaa.homepagebe.services.impl.verifyCodeServiceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RunWith(MockitoJUnitRunner.class)
public class verifyCodeServiceTest {

    @InjectMocks
    verifyCodeServiceImpl verifyCodeService;
    @InjectMocks
    String expectedVerificationCode;

    @Before
    public void configuration() {
        LocalDateTime currentTime = LocalDateTime.now();
        currentTime = currentTime.plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        expectedVerificationCode = currentTime.format(formatter) + "|" + 1;
    }

    @Test
    public void testCreateVerificationCode() {
        String actualVerificationCode = verifyCodeService.createVerificationCode(1);
        Assert.assertEquals(expectedVerificationCode, actualVerificationCode);
    }

    @Test
    public void testFormatTime() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime actualFormattedTime = verifyCodeService.formatTime(now);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String timeStr = now.format(formatter);
        LocalDateTime expectedFormattedTime = LocalDateTime.parse(timeStr, formatter);
        Assert.assertEquals(expectedFormattedTime, actualFormattedTime);
    }

    @Test
    public void testFormatTimeInStr() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String expectedTimeStr = now.format(formatter);
        String actualTimeStr = verifyCodeService.formatTimeInStr(now);
        Assert.assertEquals(expectedTimeStr, actualTimeStr);
    }

    @Test
    public void testVerificateCode() {
        boolean result = verifyCodeService.verificateCode(expectedVerificationCode);
        Assert.assertTrue(result);
    }
}
