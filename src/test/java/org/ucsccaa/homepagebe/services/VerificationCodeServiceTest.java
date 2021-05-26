package org.ucsccaa.homepagebe.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.ucsccaa.homepagebe.services.impl.SimpleVerificationCodeServiceImpl;

import java.time.LocalDateTime;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class VerificationCodeServiceTest {

    @Mock
    private DataProtection dataProtection;
    @InjectMocks
    private SimpleVerificationCodeServiceImpl verifyCodeService;

    private final Integer expectedUid = 1;
    private final String expectedVerificationCode = LocalDateTime.now().plusMinutes(5) + "|" + expectedUid;

    @Before
    public void configure() {
         when(dataProtection.encrypt(anyString())).thenReturn(expectedVerificationCode);
         when(dataProtection.decrypt(anyString())).thenReturn(expectedVerificationCode);
    }

    @Test
    public void testCreateVerificationCode() {
        String actualVerificationCode = verifyCodeService.generateVerificationCode(1);
        Assert.assertEquals(expectedVerificationCode, actualVerificationCode);
    }

    @Test
    public void testVerificationCode() {
        Integer uid = verifyCodeService.getUid(expectedVerificationCode);
        Assert.assertEquals(expectedUid, uid);
    }
}
