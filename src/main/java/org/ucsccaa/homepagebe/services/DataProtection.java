package org.ucsccaa.homepagebe.services;

public interface DataProtection {
    String encrypt(String plain);
    String decrypt(String cipher);
    <T> T encrypt(T plain);
    <T> T decrypt(T cipher);
}
