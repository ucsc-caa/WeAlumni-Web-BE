package org.ucsccaa.homepagebe.authentication;

public interface Authentication {
    String generateToken(String identifier, String password);
    Boolean validateToken(String token);
    <T> T getValue(String token, String key, Class<T> valueType);
}
