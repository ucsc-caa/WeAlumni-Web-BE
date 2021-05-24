package org.ucsccaa.homepagebe.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.ucsccaa.homepagebe.models.MailContentEditor;
import org.ucsccaa.homepagebe.services.mailSenderHandler;
import org.ucsccaa.homepagebe.services.verifyCodeService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class mailSenderHandlerImpl implements mailSenderHandler {

    private JavaMailSender mailSender;
    private String subject;
    @Autowired
    private verifyCodeService code;
    @Override
    public void sendMail(String toAddress, String subject, String URL_END_POINT, int uid) throws UnsupportedEncodingException, MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        //load mail content
        MailContentEditor mailContent = new MailContentEditor(subject, URL_END_POINT, code.createVerificationCode(uid));
        String content = mailContent.getContent();
        //send mail to the target
        helper.setFrom("zjiang940128@gmail.com", "Jzz");
        helper.setSubject(subject);
        helper.setTo(toAddress);
        helper.setText(content, true);
        mailSender.send(message);
        System.out.println("mail sent");
    }
}
