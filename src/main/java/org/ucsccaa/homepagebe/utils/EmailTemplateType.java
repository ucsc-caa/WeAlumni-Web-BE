package org.ucsccaa.homepagebe.utils;

public enum EmailTemplateType {
    BLANK(""),
    EMAIL_VERIFICATION("email_verification.txt");

    private final String templateFileName;
    EmailTemplateType(String templateFileName) {
        this.templateFileName = templateFileName;
    }

    public String getTemplateResourceLocation() {
        return "classpath:emailTemplates/" + templateFileName;
    }
}
