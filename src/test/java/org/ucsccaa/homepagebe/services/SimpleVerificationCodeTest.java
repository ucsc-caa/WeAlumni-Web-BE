package org.ucsccaa.homepagebe.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.ucsccaa.homepagebe.exceptions.customizedExceptions.VerificationCodeExpiredException;
import org.ucsccaa.homepagebe.services.impl.SimpleVerificationCodeServiceImpl;


import java.time.LocalDateTime;
@RunWith(MockitoJUnitRunner.class)
public class SimpleVerificationCodeTest {
    @Mock
    private DataProtection dataProtection;
    @InjectMocks
    SimpleVerificationCodeServiceImpl simpleVerificationCode;



    private final LocalDateTime staticTime = LocalDateTime.now().plusDays(1);
    private final Integer uid = 1;
    private final String expected_verificationCode = staticTime + "|" + uid;
    private final String broken_timStamp_code = LocalDateTime.now() + "|" + uid;
    private final String invalid_uid_code = LocalDateTime.now().plusDays(1) + "|" + "a";
    private final String incorrect_dateTime_code = "05272021jzz1";
    @Before
    public void configuration() {
        Mockito.when(dataProtection.encrypt(Mockito.anyString())).thenReturn(expected_verificationCode);
        Mockito.when(dataProtection.decrypt(Mockito.eq(expected_verificationCode))).thenReturn(expected_verificationCode);
        Mockito.when(dataProtection.decrypt(Mockito.eq(broken_timStamp_code))).thenReturn(broken_timStamp_code);
        Mockito.when(dataProtection.decrypt(Mockito.eq(invalid_uid_code))).thenReturn(invalid_uid_code);
        Mockito.when(dataProtection.decrypt(Mockito.eq(incorrect_dateTime_code))).thenReturn(incorrect_dateTime_code);
    }
    @Test
    public void generateVerificationCode() {
        String result = simpleVerificationCode.generateVerificationCode(1);
        Assert.assertEquals(expected_verificationCode, result);
    }
    @Test
    public void getUid() {
        Integer result = simpleVerificationCode.getUid(expected_verificationCode);
        Assert.assertEquals(uid, result);
    }
    @Test(expected = VerificationCodeExpiredException.class)
    public void getUid_expired_timeStamp() {
        Integer result = simpleVerificationCode.getUid(broken_timStamp_code);
    }
    @Test(expected = VerificationCodeExpiredException.class)
    public void getUid_invalid_uid() {
        Integer result = simpleVerificationCode.getUid(invalid_uid_code);
    }
    @Test(expected = RuntimeException.class)
    public void getUid_dateTimeParse_exception() {
        Integer result = simpleVerificationCode.getUid(incorrect_dateTime_code);
    }
}
