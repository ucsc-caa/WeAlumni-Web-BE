package org.ucsccaa.homepagebe.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.ucsccaa.homepagebe.domains.Member;
import org.ucsccaa.homepagebe.exceptions.customizedExceptions.ServerErrorException;
import org.ucsccaa.homepagebe.services.impl.SymmetricDataProtection;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class SymmetricDataProtectionTests {
    @InjectMocks
    private SymmetricDataProtection symmetricDataProtection;

    private final String expected_plain = "plain text";
    private final String expected_cipher = "57+ggBwJ3L3xkfTtl794fA==";

    private final Member member_plain = new Member(
            1, 1, Member.Status.PENDING, "Sammy Slug", true, LocalDate.now(), "caa@ucsc.edu", "8310000000", "wechat", 1,
            new Member.Address("1156 High Street", "Santa Cruz", "US", "95064"),
            new Member.Degree("1234567", "Bachelor", 2020, "CS", "MATH", "TIM"),
            new Member.Career(true, "UCSC", "Student"),
            true);

    private final Member member_cipher = new Member(
            1, 1, Member.Status.PENDING, "rHT9++CT++KD7aLXhaDDmQ==", true, LocalDate.now(), "7Njxp5tHQarD0VwTQMhsyA==", "YCXNC5+SGii1RmzyvLa/0w==", "ZYnwxFSfOtmUJjigXDXf/g==", 1,
            new Member.Address("cDmUx2XfF7tPdTB68/zJ03zI1M+YPUhdYCTeGH4Hl04=", "mKsENmU3rHDWX+eBbZakCw==", "7trGa0aZvkdIGlfy9nBBug==", "xRkg4nuZH4qZTBeJ/hkCpw=="),
            new Member.Degree("yxMATU9KJbepryM7FZ6Thg==", "8n2er2k4vWQrFyuAGjdCig==", 2020, "NofQ+u8GDDSbUgaaJ9Qygw==", "DHTUXVxtNH2PyB7zElE/jw==", "1HU0CtqtyFJrikgKPhxGYw=="),
            new Member.Career(true, "YKI/eXc2gaJZspNhGXIsOg==", "LjnLXIveZYEstSepckvZMQ=="),
            true);

    @Before
    public void configure() throws Exception {
        ReflectionTestUtils.setField(symmetricDataProtection, "secret", "secret0123456789");
        symmetricDataProtection.configureCipher();
    }

    @Test
    public void testEncryptString() {
        String result = symmetricDataProtection.encrypt(expected_plain);
        assertNotNull(result);
        assertEquals(expected_cipher, result);
    }

    @Test
    public void testEncryptString_nullInput() {
        assertNull(symmetricDataProtection.encrypt(null));
    }

    @Test
    public void testEncryptString_emptyStringInput() {
        assertNull(symmetricDataProtection.encrypt(""));
    }

    @Test(expected = ServerErrorException.class)
    public void testEncryptString_exceptionOccurs() throws Exception {
        ReflectionTestUtils.setField(symmetricDataProtection, "encryptCipher", Cipher.getInstance("AES/ECB/PKCS5Padding"));
        assertNull(symmetricDataProtection.encrypt(expected_plain));
    }

    @Test
    public void testDecryptString() {
        String result = symmetricDataProtection.decrypt(expected_cipher);
        assertNotNull(result);
        assertEquals(expected_plain, result);
    }

    @Test
    public void testDecryptString_nullInput() {
        assertNull(symmetricDataProtection.decrypt(null));
    }

    @Test
    public void testDecryptString_emptyStringInput() {
        assertNull(symmetricDataProtection.decrypt(""));
    }

    @Test(expected = ServerErrorException.class)
    public void testDecryptString_exceptionOccurs() throws Exception {
        ReflectionTestUtils.setField(symmetricDataProtection, "decryptCipher", Cipher.getInstance("AES/ECB/PKCS5Padding"));
        assertNull(symmetricDataProtection.decrypt(expected_cipher));
    }

    @Test
    public void testEncryptObject() {
        Member result = symmetricDataProtection.encrypt(member_plain);
        assertNotNull(result);
        assertEquals(member_cipher, result);
    }

    @Test
    public void testEncryptObject_nullInput() {
        assertNull(symmetricDataProtection.encrypt((Member) null));
    }

    @Test(expected = ServerErrorException.class)
    public void testEncryptObject_unencrytable() {
        symmetricDataProtection.encrypt(1234567);
    }

    @Test(expected = ServerErrorException.class)
    public void testEncryptObject_exceptionOccurs() throws Exception {
        ReflectionTestUtils.setField(symmetricDataProtection, "encryptCipher", Cipher.getInstance("AES/ECB/PKCS5Padding"));
        symmetricDataProtection.encrypt(member_plain);
    }

    @Test
    public void testDecryptObject() {
        Member result = symmetricDataProtection.decrypt(member_cipher);
        assertNotNull(result);
        assertEquals(member_plain, result);
    }

    @Test(expected = ServerErrorException.class)
    public void testDecryptObject_exceptionOccurs() throws Exception {
        ReflectionTestUtils.setField(symmetricDataProtection, "decryptCipher", Cipher.getInstance("AES/ECB/PKCS5Padding"));
        symmetricDataProtection.decrypt(member_cipher);
    }
}
