package org.ucsccaa.homepagebe.services.impl;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.ucsccaa.homepagebe.exceptions.customizedExceptions.ServerErrorException;
import org.ucsccaa.homepagebe.services.DataProtection;
import org.ucsccaa.homepagebe.utils.Encryptable;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

@Service
public class SymmetricDataProtection implements DataProtection {

    @Value("${protection.data.secret:UCSCCAADEVSECRET}")
    private String secret;

    private final Cipher encryptCipher;
    private final Cipher decryptCipher;

    @PostConstruct
    public void configureCipher() throws Exception {
        encryptCipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(secret.getBytes(), "AES"));
        decryptCipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secret.getBytes(), "AES"));
    }

    public SymmetricDataProtection() throws NoSuchPaddingException, NoSuchAlgorithmException {
        encryptCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        decryptCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    }

    @Override
    public String encrypt(String plain) {
        try {
            return _encrypt(plain);
        } catch (Exception e) {
            throw new ServerErrorException("Failed to encrypt: plain - " + plain);
        }
    }

    @Override
    public String decrypt(String cipher) {
        try {
            return _decrypt(cipher);
        } catch (Exception e) {
            throw new ServerErrorException("Failed to decrypt: cipher - " + cipher);
        }
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
                    field.set(plain, _encrypt((String) field.get(plain)));
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
        if (cipher == null)
            return null;
        Field[] fields = cipher.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getType() == String.class) {
                    String ans = _decrypt((String) field.get(cipher));
                    field.set(cipher, ans);
                }else if (field.getType().isAnnotationPresent(Encryptable.class))
                    field.set(cipher, decrypt(field.get(cipher)));
            }
        } catch (Exception e) {
            throw new ServerErrorException("Failed to decrypt data: type - " + cipher.getClass() + ", e - " + e.getMessage());
        }

        return cipher;
    }

    private String _encrypt(String plain) throws Exception {
        if (StringUtils.isEmpty(plain))
            return null;

        byte[] b = encryptCipher.doFinal(plain.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeBase64String(b);
    }

    private String _decrypt(String cipher) throws Exception {
        if (StringUtils.isEmpty(cipher))
            return null;

        // Use base64 algorithm to decode, avoiding Chinese garbled codes
        byte[] cipherBytes = Base64.decodeBase64(cipher);
        byte[] decryptBytes = decryptCipher.doFinal(cipherBytes);
        return new String(decryptBytes);
    }
}