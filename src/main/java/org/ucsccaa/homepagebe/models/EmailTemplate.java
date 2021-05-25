package org.ucsccaa.homepagebe.models;

import lombok.Data;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Data
public class EmailTemplate {
    private String sender;
    private String senderName;
    private String receiver;
    private String receiverName;
    private String subject;
    private String content;
    private List<String> placeholderValues;

    public void setEmailContent(MimeMessageHelper helper) throws UnsupportedEncodingException, MessagingException {
        helper.setFrom(sender, senderName);
        helper.setSubject(subject);
        helper.setTo(receiver);
        helper.setText(content, true);
    }
}
