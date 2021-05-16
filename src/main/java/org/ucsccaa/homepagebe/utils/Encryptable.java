package org.ucsccaa.homepagebe.utils;

import java.lang.annotation.*;

/**
 * Indicates that this class/object is encryptable for DataProtection. This class itself
 * must contain properties that could be encrypted, more specifically, it must have at
 * least one String property to be encrypted.
 *
 * This annotation will also be used in decryption process having the same function.
 *
 * Add this annotation to any POJO class and DataProtection will allow its properties
 * to be encrypted recursively. If an annotated class contains and un-annotated class,
 * then the =properties within this class will be encrypted but that un-annotated class
 * will not be encrypted.
 *
 * @author Hang Yuan
 * @since 0.1
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Encryptable {

}
