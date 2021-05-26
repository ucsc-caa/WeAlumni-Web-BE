package org.ucsccaa.homepagebe.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.ucsccaa.homepagebe.exceptions.customizedExceptions.ServerErrorException;
import org.ucsccaa.homepagebe.models.EmailTemplate;

import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JavaMailSender mailSender;

    public void sendMail(EmailTemplate emailTemplate) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            emailTemplate.setEmailContent(helper);
            mailSender.send(message);
            logger.info("Email sent to: {}", emailTemplate.getReceiver());
        } catch (Exception e) {
            logger.error("Failed to send out email: e - {}", e.getMessage());
            throw new ServerErrorException("EmailService failed to send out email");
        }
    }
}
