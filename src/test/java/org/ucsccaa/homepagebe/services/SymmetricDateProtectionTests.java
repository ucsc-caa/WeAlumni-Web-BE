package org.ucsccaa.homepagebe.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.ucsccaa.homepagebe.domains.Member;
import org.ucsccaa.homepagebe.services.impl.SymmetricDataProtection;

import java.time.LocalDate;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class SymmetricDateProtectionTests {
    @InjectMocks
    private SymmetricDataProtection symmetricDataProtection;

    private final String expected_plain = "plain text";
    private final String expected_cipher = "plain textsecret";

    private final Member member_plain = new Member(
            1, 1, Member.Status.PENDING, "Sammy Slug", true, LocalDate.now(), "caa@ucsc.edu", "8310000000", "wechat", 1,
            new Member.Address("1156 High Street", "Santa Cruz", "US", "95064"),
            new Member.Degree("1234567", "Bachelor", 2020, "CS", "MATH", "TIM"),
            new Member.Career(true, "UCSC", "Student"),
            true);

    private final Member member_cipher = new Member(
            1, 1, Member.Status.PENDING, "Sammy Slugsecret", true, LocalDate.now(), "caa@ucsc.edusecret", "8310000000secret", "wechatsecret", 1,
            new Member.Address("1156 High Streetsecret", "Santa Cruzsecret", "USsecret", "95064secret"),
            new Member.Degree("1234567secret", "Bachelorsecret", 2020, "CSsecret", "MATHsecret", "TIMsecret"),
            new Member.Career(true, "UCSCsecret", "Studentsecret"),
            true);

    @Before
    public void configure() {
        ReflectionTestUtils.setField(symmetricDataProtection, "secret", "secret");
    }

    @Test
    public void testEncryptString() {
        assertNotNull(symmetricDataProtection.encrypt(expected_plain));
    }

    @Test
    public void testDecryptString() {
        assertNotNull(symmetricDataProtection.decrypt(expected_cipher));
    }

    @Test
    public void testEncryptObject() {
        assertNotNull(symmetricDataProtection.encrypt(member_plain));
    }

    @Test
    public void testDecryptObject() {
        assertNotNull(symmetricDataProtection.decrypt(member_cipher));
    }
}
