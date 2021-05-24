package org.ucsccaa.homepagebe.services;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface mailSenderHandler {
    void sendMail(String toAddress, String subject, String URL_END_POINT, int uid) throws UnsupportedEncodingException, MessagingException;
}
