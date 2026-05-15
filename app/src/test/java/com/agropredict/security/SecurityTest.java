package com.agropredict.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import com.agropredict.domain.authentication.attempt.ILoginAttempt;
import com.agropredict.domain.authentication.attempt.InitialAttempt;
import com.agropredict.domain.authentication.gate.LoginGate;
import com.agropredict.domain.authentication.gate.LoginRejectedException;
import com.agropredict.domain.authentication.session.Session;
import com.agropredict.application.input_validation.EmailValidator;
import com.agropredict.application.input_validation.FullNameValidator;
import com.agropredict.application.input_validation.PasswordValidator;
import com.agropredict.application.input_validation.UsernameValidator;
import com.agropredict.domain.input_validation.ValidatorTester;
import com.agropredict.factory.StubEmailFailureFactory;
import com.agropredict.factory.StubFullNameFailureFactory;
import com.agropredict.factory.StubPasswordFailureFactory;
import com.agropredict.factory.StubUsernameFailureFactory;
import com.agropredict.infrastructure.security.PasswordHasher;
import com.agropredict.visitor.LimitExpecter;
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
        ILoginAttempt attempt = blockedAttempt(now);
        assertEquals("blocked", evaluate(attempt, now));
        assertEquals("blocked", evaluate(attempt, now + 4 * 60 * 1000));
    }

    @Test
    public void testBlockDurationExactlyFiveMinutes() {
        long now = 1000000L;
        ILoginAttempt attempt = blockedAttempt(now);
        assertEquals("blocked", evaluate(attempt, now + 5 * 60 * 1000));
        assertNotEquals("blocked", evaluate(attempt, now + 5 * 60 * 1000 + 1));
    }

    @Test
    public void testAttemptsResetAfterBlockExpires() {
        long now = System.currentTimeMillis();
        ILoginAttempt attempt = blockedAttempt(now);
        long afterExpiry = now + 5 * 60 * 1000 + 1;
        ILoginAttempt reset = attempt.fail(afterExpiry);
        assertEquals("allow", evaluate(reset, afterExpiry));
    }

    @Test
    public void testCannotBypassLockoutByKeepingTrying() {
        long now = System.currentTimeMillis();
        ILoginAttempt attempt = blockedAttempt(now);
        ILoginAttempt stillBlocked = attempt.fail(now + 1000);
        assertEquals("blocked", evaluate(stillBlocked, now + 1000));
    }

    @Test
    public void testSqlInjectionInEmail() {
        ValidatorTester tester = new ValidatorTester(new EmailValidator(new StubEmailFailureFactory()));
        tester.rejects("'; DROP TABLE users;--@evil.com");
        tester.rejects("admin'--@evil.com");
        tester.rejects("1=1@evil.com");
        tester.rejects("OR 1=1--@evil.com");
    }

    @Test
    public void testSqlInjectionInUsername() {
        ValidatorTester tester = new ValidatorTester(new UsernameValidator(new StubUsernameFailureFactory()));
        tester.rejects("admin'--");
        tester.rejects("'; DROP TABLE");
        tester.rejects("1=1; --");
        tester.rejects("UNION SELECT *");
    }

    @Test
    public void testSqlInjectionInPassword() {
        new ValidatorTester(new PasswordValidator(new StubPasswordFailureFactory())).accepts("'; DROP1!aXY");
    }

    @Test
    public void testXssInEmail() {
        ValidatorTester tester = new ValidatorTester(new EmailValidator(new StubEmailFailureFactory()));
        tester.rejects("<script>alert(1)</script>@evil.com");
        tester.rejects("user<img>@evil.com");
        tester.rejects("user<svg/onload=alert(1)>@evil.com");
    }

    @Test
    public void testXssInUsername() {
        ValidatorTester tester = new ValidatorTester(new UsernameValidator(new StubUsernameFailureFactory()));
        tester.rejects("<script>alert(1)</script>");
        tester.rejects("user<img src=x>");
        tester.rejects("onmouseover=alert(1)");
    }

    @Test
    public void testXssInFullName() {
        ValidatorTester tester = new ValidatorTester(new FullNameValidator(new StubFullNameFailureFactory()));
        tester.rejects("<script>alert(1)</script>");
        tester.rejects("Juan<img>Perez");
        tester.rejects("alert('xss')");
    }

    @Test
    public void testPasswordComplexityEnforced() {
        ValidatorTester tester = new ValidatorTester(new PasswordValidator(new StubPasswordFailureFactory()));
        tester.rejects("passwordxxxx");
        tester.rejects("123456789012");
        tester.rejects("ABCDEFGHIJKL");
        tester.rejects("abcdefghijkl");
        tester.rejects("Abcdefghijkl");
        tester.rejects("Abcdef1hijkl");
    }

    @Test
    public void testPasswordMinimumLengthEnforced() {
        ValidatorTester tester = new ValidatorTester(new PasswordValidator(new StubPasswordFailureFactory()));
        tester.rejects("Aa1!");
        tester.rejects("Aa1!xxx");
        tester.rejects("Aa1!xxxxxxx");
        tester.accepts("Aa1!xxxxxxxx");
    }

    @Test
    public void testNullOccupationDoesNotGrantAdvancedAccess() {
        new Session("user_1", null).observe(new LimitExpecter());
    }

    @Test
    public void testEmptyOccupationDoesNotGrantAdvancedAccess() {
        new Session("user_1", "").observe(new LimitExpecter());
    }

    @Test
    public void testUnknownOccupationDoesNotGrantAdvancedAccess() {
        new Session("user_1", "Hacker").observe(new LimitExpecter());
    }

    @Test
    public void testCaseSensitiveRoleCheck() {
        new Session("user_1", "agronomist").observe(new LimitExpecter());
    }

    @Test
    public void testNullSessionIdentifierRejected() {
        org.junit.Assert.assertThrows(IllegalArgumentException.class, () -> new Session(null, "Farmer"));
    }

    @Test
    public void testEmptySessionIdentifierRejected() {
        org.junit.Assert.assertThrows(IllegalArgumentException.class, () -> new Session("", "Farmer"));
    }

    @Test
    public void testNullEmailRejected() {
        new ValidatorTester(new EmailValidator(new StubEmailFailureFactory())).rejects(null);
    }

    @Test
    public void testNullUsernameRejected() {
        new ValidatorTester(new UsernameValidator(new StubUsernameFailureFactory())).rejects(null);
    }

    @Test
    public void testNullPasswordRejected() {
        new ValidatorTester(new PasswordValidator(new StubPasswordFailureFactory())).rejects(null);
    }

    @Test
    public void testNullFullNameRejected() {
        new ValidatorTester(new FullNameValidator(new StubFullNameFailureFactory())).rejects(null);
    }

    private ILoginAttempt blockedAttempt(long now) {
        ILoginAttempt attempt = new InitialAttempt();
        for (int count = 0; count < 5; count++) attempt = attempt.fail(now);
        return attempt;
    }

    private String evaluate(ILoginAttempt attempt, long time) {
        LoginGate gate = new LoginGate(
            callback -> callback.receive("blocked"),
            callback -> callback.receive("exhausted"));
        try {
            attempt.evaluate(time, gate);
            return "allow";
        } catch (LoginRejectedException rejected) {
            return rejected.getMessage();
        }
    }
}
