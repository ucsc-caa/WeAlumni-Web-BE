package org.ucsccaa.homepagebe.services.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.ucsccaa.homepagebe.exceptions.customizedExceptions.ServerErrorException;
import org.ucsccaa.homepagebe.services.DataProtection;
import org.ucsccaa.homepagebe.utils.Encryptable;

import java.lang.reflect.Field;

@Service
public class SymmetricDataProtection implements DataProtection {

    @Value("${protection.data.secret:UCSCCAASIGNINGKEY}")
    private String secret;

    @Override
    public String encrypt(String plain) {
        return encrypt(plain, secret);
    }

    @Override
    public String decrypt(String cipher) {
        return decrypt(cipher, secret);
    }

    @Override
    public <T> T encrypt(T plain) {
        if (plain == null)
            return null;
        if (!(plain.getClass().isAnnotationPresent(Encryptable.class)))
            throw new ServerErrorException("Class is not encryptable: type - " + plain.getClass());

        Field[] fields = plain.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getType() == String.class)
                    field.set(plain, encrypt((String) field.get(plain), secret));
                else if (field.getType().isAnnotationPresent(Encryptable.class))
                    field.set(plain, encrypt(field.get(plain)));
            }
        } catch (Exception e) {
            throw new ServerErrorException("Failed to encrypt data: type - " + plain.getClass() + ", e - " + e.getMessage());
        }

        return plain;
    }

    @Override
    public <T> T decrypt(T cipher) {
        Field[] fields = cipher.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getType() == String.class)
                    field.set(cipher, decrypt((String) field.get(cipher), secret));
                else if (field.getType().isAnnotationPresent(Encryptable.class))
                    field.set(cipher, decrypt(field.get(cipher)));
            }
        } catch (Exception e) {
            throw new ServerErrorException("Failed to decrypt data: type - " + cipher.getClass() + ", e - " + e.getMessage());
        }

        return cipher;
    }

    private String encrypt(String plain, String secret) {
        if (StringUtils.isEmpty(plain))
            return null;
        // TODO: add actual data encryption logic
        // ATTENTION: BELOW LOGIC IS TEMPORARY BLOCK FOR TEST PURPOSE　ONLY
        try {
            plain = plain.concat(secret);
        } catch (Exception e) {
            System.out.println("FAILED: plain=" + plain);
        }
        return plain;
    }

    private String decrypt(String cipher, String secret) {
        if (StringUtils.isEmpty(cipher))
            return null;
        // TODO: add actual data decryption logic
        // ATTENTION: BELOW LOGIC IS TEMPORARY BLOCK FOR TEST PURPOSE　ONLY
        try {
            cipher = cipher.substring(0, cipher.length()-6);
        } catch (Exception e) {
            System.out.println("FAILED: cipher=" + cipher);
        }
        return cipher;
    }
}