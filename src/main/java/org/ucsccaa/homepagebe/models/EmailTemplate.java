package org.ucsccaa.homepagebe.models;

import lombok.Data;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.ResourceUtils;
import org.ucsccaa.homepagebe.exceptions.customizedExceptions.ServerErrorException;
import org.ucsccaa.homepagebe.utils.EmailTemplateType;

import javax.mail.MessagingException;
import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class EmailTemplate {
    private String sender;
    private String senderName;
    private String receiver;
    private String receiverName;
    private String subject;
    private String content = "";
    private Map<String, String> placeholderValues;

    public EmailTemplate(EmailTemplateType type) {
        if (type == EmailTemplateType.BLANK) return;
        try {
            File file = ResourceUtils.getFile(type.getTemplateResourceLocation());
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            content = reader.lines().reduce(String::concat).orElse("");
        } catch (Exception e) {
            throw new ServerErrorException("Not able to generate email: e - " + e.getMessage());
        }
    }

    public void setEmailContent(MimeMessageHelper helper) throws UnsupportedEncodingException, MessagingException {
        helper.setFrom(sender, senderName);
        helper.setSubject(subject);
        helper.setTo(receiver);
        content = content.replace("{RECEIVER_NAME}", receiverName);
        for (Map.Entry<String, String> entry : placeholderValues.entrySet()) {
            content = content.replace(entry.getKey(), entry.getValue());
        }
        helper.setText(content, true);
    }

    public void addPlaceholder(String key, String value) {
        if (placeholderValues == null) {
            placeholderValues = new LinkedHashMap<>();
        }
        placeholderValues.put(key, value);
    }
}
