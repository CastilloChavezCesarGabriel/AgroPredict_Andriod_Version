package com.agropredict.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import com.agropredict.domain.CapturingLoginGate;
import com.agropredict.domain.LoginAttempt;
import com.agropredict.domain.Session;
import com.agropredict.domain.input_validation.EmailValidator;
import com.agropredict.domain.input_validation.FullNameValidator;
import com.agropredict.domain.input_validation.PasswordValidator;
import com.agropredict.domain.input_validation.UsernameValidator;
import com.agropredict.infrastructure.security.PasswordHasher;
import com.agropredict.visitor.TestOccupationVisitor;
import org.junit.Test;

public final class SecurityTest {
    @Test
    public void testPasswordNeverStoredInPlaintext() {
        PasswordHasher hasher = new PasswordHasher();
        String hash = hasher.hash("MySecretPass1!");
        assertFalse(hash.contains("MySecretPass1!"));
    }

    @Test
    public void testSaltUniqueness() {
        PasswordHasher hasher = new PasswordHasher();
        String hash1 = hasher.hash("Same1Pass!");
        String hash2 = hasher.hash("Same1Pass!");
        String salt1 = hash1.split(":")[0];
        String salt2 = hash2.split(":")[0];
        assertNotEquals(salt1, salt2);
    }

    @Test
    public void testHashOutputLength() {
        PasswordHasher hasher = new PasswordHasher();
        String hash = hasher.hash("Test1234!");
        String[] parts = hash.split(":");
        assertEquals("Salt should be 32 hex chars", 32, parts[0].length());
        assertEquals("Key should be 64 hex chars", 64, parts[1].length());
    }

    @Test
    public void testHashAlgorithmProducesDifferentOutputForDifferentPasswords() {
        PasswordHasher hasher = new PasswordHasher();
        String hash1 = hasher.hash("Password1!");
        String hash2 = hasher.hash("Password2!");
        String key1 = hash1.split(":")[1];
        String key2 = hash2.split(":")[1];
        assertNotEquals(key1, key2);
    }

    @Test
    public void testDifferentUsersWithSamePasswordGetDifferentHashes() {
        PasswordHasher hasher = new PasswordHasher();
        String userAHash = hasher.hash("SharedPass1!");
        String userBHash = hasher.hash("SharedPass1!");
        assertNotEquals(userAHash, userBHash);
        assertTrue(hasher.verify("SharedPass1!", userAHash));
        assertTrue(hasher.verify("SharedPass1!", userBHash));
    }

    @Test
    public void testTimingConstantComparison() {
        PasswordHasher hasher = new PasswordHasher();
        String hash = hasher.hash("Test1234!");
        long totalCorrect = 0;
        long totalWrong = 0;
        int iterations = 5;
        for (int round = 0; round < iterations; round++) {
            long startCorrect = System.nanoTime();
            hasher.verify("Test1234!", hash);
            totalCorrect += System.nanoTime() - startCorrect;

            long startWrong = System.nanoTime();
            hasher.verify("Wrong123!", hash);
            totalWrong += System.nanoTime() - startWrong;
        }
        double ratio = (double) totalCorrect / totalWrong;
        assertTrue("Timing should be similar to prevent timing attacks", ratio > 0.3 && ratio < 3.0);
    }

    @Test
    public void testMalformedHashRejected() {
        PasswordHasher hasher = new PasswordHasher();
        assertFalse(hasher.verify("password", "not_a_valid_hash"));
        assertFalse(hasher.verify("password", ""));
        assertFalse(hasher.verify("password", ":::"));
        assertFalse(hasher.verify("password", "abc:def:ghi"));
    }

    @Test
    public void testEmptyPasswordHashedSafely() {
        PasswordHasher hasher = new PasswordHasher();
        String hash = hasher.hash("");
        assertTrue(hasher.verify("", hash));
        assertFalse(hasher.verify("notempty", hash));
    }

    @Test
    public void testBruteForceProtection() {
        long now = System.currentTimeMillis();
        LoginAttempt attempt = new LoginAttempt(0, 0);
        for (int count = 0; count < 5; count++) attempt = attempt.fail(now);
        CapturingLoginGate immediate = new CapturingLoginGate();
        attempt.evaluate(now, immediate);
        assertTrue(immediate.hasReceived("block"));
        CapturingLoginGate later = new CapturingLoginGate();
        attempt.evaluate(now + 4 * 60 * 1000, later);
        assertTrue(later.hasReceived("block"));
    }

    @Test
    public void testBlockDurationExactlyFiveMinutes() {
        long now = 1000000L;
        LoginAttempt attempt = new LoginAttempt(0, 0);
        for (int count = 0; count < 5; count++) attempt = attempt.fail(now);
        CapturingLoginGate atBoundary = new CapturingLoginGate();
        attempt.evaluate(now + 5 * 60 * 1000, atBoundary);
        assertTrue(atBoundary.hasReceived("block"));
        CapturingLoginGate justAfter = new CapturingLoginGate();
        attempt.evaluate(now + 5 * 60 * 1000 + 1, justAfter);
        assertFalse(justAfter.hasReceived("block"));
    }

    @Test
    public void testAttemptsResetAfterBlockExpires() {
        long now = System.currentTimeMillis();
        LoginAttempt attempt = new LoginAttempt(0, 0);
        for (int count = 0; count < 5; count++) attempt = attempt.fail(now);
        long afterExpiry = now + 5 * 60 * 1000 + 1;
        LoginAttempt reset = attempt.fail(afterExpiry);
        CapturingLoginGate gate = new CapturingLoginGate();
        reset.evaluate(afterExpiry, gate);
        assertTrue(gate.hasReceived("allow"));
    }

    @Test
    public void testCannotBypassLockoutByKeepingTrying() {
        long now = System.currentTimeMillis();
        LoginAttempt attempt = new LoginAttempt(0, 0);
        for (int count = 0; count < 5; count++) attempt = attempt.fail(now);
        LoginAttempt stillBlocked = attempt.fail(now + 1000);
        CapturingLoginGate gate = new CapturingLoginGate();
        stillBlocked.evaluate(now + 1000, gate);
        assertTrue(gate.hasReceived("block"));
    }

    @Test
    public void testSqlInjectionInEmail() {
        EmailValidator validator = new EmailValidator();
        assertFalse(validator.isValid("'; DROP TABLE users;--@evil.com"));
        assertFalse(validator.isValid("admin'--@evil.com"));
        assertFalse(validator.isValid("1=1@evil.com"));
        assertFalse(validator.isValid("OR 1=1--@evil.com"));
    }

    @Test
    public void testSqlInjectionInUsername() {
        UsernameValidator validator = new UsernameValidator();
        assertFalse(validator.isValid("admin'--"));
        assertFalse(validator.isValid("'; DROP TABLE"));
        assertFalse(validator.isValid("1=1; --"));
        assertFalse(validator.isValid("UNION SELECT *"));
    }

    @Test
    public void testSqlInjectionInPassword() {
        PasswordValidator validator = new PasswordValidator();
        assertTrue(validator.isValid("'; DROP1!a"));
    }

    @Test
    public void testXssInEmail() {
        EmailValidator validator = new EmailValidator();
        assertFalse(validator.isValid("<script>alert(1)</script>@evil.com"));
        assertFalse(validator.isValid("user<img>@evil.com"));
        assertFalse(validator.isValid("user<svg/onload=alert(1)>@evil.com"));
    }

    @Test
    public void testXssInUsername() {
        UsernameValidator validator = new UsernameValidator();
        assertFalse(validator.isValid("<script>alert(1)</script>"));
        assertFalse(validator.isValid("user<img src=x>"));
        assertFalse(validator.isValid("onmouseover=alert(1)"));
    }

    @Test
    public void testXssInFullName() {
        FullNameValidator validator = new FullNameValidator();
        assertFalse(validator.isValid("<script>alert(1)</script>"));
        assertFalse(validator.isValid("Juan<img>Perez"));
        assertFalse(validator.isValid("alert('xss')"));
    }

    @Test
    public void testPasswordComplexityEnforced() {
        PasswordValidator validator = new PasswordValidator();
        assertFalse(validator.isValid("password"));
        assertFalse(validator.isValid("12345678"));
        assertFalse(validator.isValid("ABCDEFGH"));
        assertFalse(validator.isValid("abcdefgh"));
        assertFalse(validator.isValid("Abcdefgh"));
        assertFalse(validator.isValid("Abcdef1h"));
    }

    @Test
    public void testPasswordMinimumLengthEnforced() {
        PasswordValidator validator = new PasswordValidator();
        assertFalse(validator.isValid("Aa1!"));
        assertFalse(validator.isValid("Aa1!xxx"));
        assertTrue(validator.isValid("Aa1!xxxx"));
    }

    @Test
    public void testNullOccupationDoesNotGrantAdvancedAccess() {
        TestOccupationVisitor handler = new TestOccupationVisitor();
        new Session("user_1", null).observe(handler);
        assertFalse(handler.sawAdvanced());
    }

    @Test
    public void testEmptyOccupationDoesNotGrantAdvancedAccess() {
        TestOccupationVisitor handler = new TestOccupationVisitor();
        new Session("user_1", "").observe(handler);
        assertFalse(handler.sawAdvanced());
    }

    @Test
    public void testUnknownOccupationDoesNotGrantAdvancedAccess() {
        TestOccupationVisitor handler = new TestOccupationVisitor();
        new Session("user_1", "Hacker").observe(handler);
        assertFalse(handler.sawAdvanced());
    }

    @Test
    public void testCaseSensitiveRoleCheck() {
        TestOccupationVisitor handler = new TestOccupationVisitor();
        new Session("user_1", "agronomist").observe(handler);
        assertFalse(handler.sawAdvanced());
    }

    @Test
    public void testNullSessionIdentifierIsInactive() {
        Session session = new Session(null, "Farmer");
        assertFalse(session.isActive());
    }

    @Test
    public void testEmptySessionIdentifierIsInactive() {
        Session session = new Session("", "Farmer");
        assertFalse(session.isActive());
    }

    @Test
    public void testNullEmailRejected() {
        assertFalse(new EmailValidator().isValid(null));
    }

    @Test
    public void testNullUsernameRejected() {
        assertFalse(new UsernameValidator().isValid(null));
    }

    @Test
    public void testNullPasswordRejected() {
        assertFalse(new PasswordValidator().isValid(null));
    }

    @Test
    public void testNullFullNameRejected() {
        assertFalse(new FullNameValidator().isValid(null));
    }
}