package org.ucsccaa.homepagebe.services.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.ucsccaa.homepagebe.services.DataProtection;

@Service
public class SymmetricProtection implements DataProtection {
    @Value("${data.secret}")
    private String secret;

    @Override
    public String encrypt(String plain) {
        return encrypt(plain, secret);
    }

    @Override
    public String decrypt(String cipher) {
        return decrypt(cipher, secret);
    }

    private String encrypt(String plain, String secret) {
        if (StringUtils.isEmpty(plain))
            return null;
        // TODO: add actual data encryption logic
        return plain;
    }

    private String decrypt(String cipher, String secret) {
        if (StringUtils.isEmpty(cipher))
            return null;
        // TODO: add actual data decryption logic
        return cipher;
    }
}
