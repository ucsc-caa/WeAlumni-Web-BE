package org.ucsccaa.homepagebe.services;

public interface DataProtection {
    String encrypt(String plain);
    String decrypt(String cipher);
}
