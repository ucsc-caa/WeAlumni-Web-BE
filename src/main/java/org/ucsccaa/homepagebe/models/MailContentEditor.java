package org.ucsccaa.homepagebe.models;

import lombok.Data;
import org.ucsccaa.homepagebe.services.DataProtection;

@Data
public class MailContentEditor {
    private String content;
    private String subject;
    private String URL_END_POINT;
    private String verificationCode;
    private DataProtection dataProtection;
    public MailContentEditor(String subject, String URL_END_POINT, String verificationCode) {
        this.subject = subject;
        this.URL_END_POINT = URL_END_POINT;
        this.verificationCode = verificationCode;
        if (subject.contains("verify") || URL_END_POINT.contains("verify")) {
            setVerifyContent(verificationCode);
        }
        if (subject.contains("revise") || URL_END_POINT.contains("revise")) {
            setReviseContent();
        }
    }

    public void setVerifyContent(String verificationCode) {
        verificationCode = dataProtection.encrypt(verificationCode);
        content = "Dear customer,<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\">"+ verificationCode + "</a></h3>"
                + "Thank you,<br>"
                + "Jzz.";
        content = content.replace("[[URL]]", "www.google.com" );
        System.out.println("www.ec2-13-57-229-60.us-west-1.compute.amazonaws.com:8080/user/" + URL_END_POINT + "{" + verificationCode + "}");
    }
    public void setReviseContent() {
        content = "Dear customer,<br>"
                + "Please click the link below to revise your email/password"
                + "<h3><a href=\"[[URL]]\">"+ subject + "</a></h3>"
                + "Thank you,<br>"
                + "Jzz.";
        content = content.replace("[[URL]]", "ec2-13-57-229-60.us-west-1.compute.amazonaws.com:8080/user/" + URL_END_POINT);
    }
    public String getSubject() {
        return subject;
    }
    public String getURL_End_Point() {
        return URL_END_POINT;
    }
    public String getContent() {
        return content;
    }
}
