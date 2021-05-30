package org.ucsccaa.homepagebe.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.javamail.JavaMailSender;
import org.ucsccaa.homepagebe.models.EmailTemplate;


@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {
    @Mock
    private JavaMailSender mailSender;
    @InjectMocks
    private EmailService emailService;
    private EmailTemplate emailTemplate;
    @Test(expected = Exception.class)
    public void sendMail_failed_test() {
        Mockito.when(mailSender.createMimeMessage()).thenReturn(null);
        emailService.sendMail(emailTemplate);
    }

}
