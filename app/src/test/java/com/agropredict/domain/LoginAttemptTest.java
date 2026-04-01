package com.agropredict.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public final class LoginAttemptTest {
    @Test
    public void testFreshAttemptNotBlocked() {
        LoginAttempt attempt = new LoginAttempt();
        assertFalse(attempt.isBlocked(System.currentTimeMillis()));
    }

    @Test
    public void testFreshAttemptNotExhausted() {
        LoginAttempt attempt = new LoginAttempt();
        assertFalse(attempt.isExhausted());
    }

    @Test
    public void testSingleFailureNotExhausted() {
        long now = System.currentTimeMillis();
        LoginAttempt attempt = new LoginAttempt().fail(now);
        assertFalse(attempt.isExhausted());
    }

    @Test
    public void testFourFailuresNotExhausted() {
        long now = System.currentTimeMillis();
        LoginAttempt attempt = new LoginAttempt();
        for (int count = 0; count < 4; count++) attempt = attempt.fail(now);
        assertFalse(attempt.isExhausted());
    }

    @Test
    public void testFiveFailuresExhausted() {
        long now = System.currentTimeMillis();
        LoginAttempt attempt = new LoginAttempt();
        for (int count = 0; count < 5; count++) attempt = attempt.fail(now);
        assertTrue(attempt.isExhausted());
    }

    @Test
    public void testBlockedAfterFiveFailures() {
        long now = System.currentTimeMillis();
        LoginAttempt attempt = new LoginAttempt();
        for (int count = 0; count < 5; count++) attempt = attempt.fail(now);
        assertTrue(attempt.isBlocked(now));
    }

    @Test
    public void testBlockedDuringWindow() {
        long now = System.currentTimeMillis();
        LoginAttempt attempt = new LoginAttempt();
        for (int count = 0; count < 5; count++) attempt = attempt.fail(now);
        assertTrue(attempt.isBlocked(now + 4 * 60 * 1000));
    }

    @Test
    public void testUnblockedAfterFiveMinutes() {
        long now = System.currentTimeMillis();
        LoginAttempt attempt = new LoginAttempt();
        for (int count = 0; count < 5; count++) attempt = attempt.fail(now);
        long afterBlock = now + 5 * 60 * 1000 + 1;
        assertFalse(attempt.isBlocked(afterBlock));
    }

    @Test
    public void testSuccessResetsAttempts() {
        long now = System.currentTimeMillis();
        LoginAttempt attempt = new LoginAttempt();
        for (int count = 0; count < 3; count++) attempt = attempt.fail(now);
        attempt = attempt.succeed();
        assertFalse(attempt.isExhausted());
        assertFalse(attempt.isBlocked(now));
    }

    @Test
    public void testFailWhileBlockedDoesNotIncrement() {
        long now = System.currentTimeMillis();
        LoginAttempt attempt = new LoginAttempt();
        for (int count = 0; count < 5; count++) attempt = attempt.fail(now);
        LoginAttempt blocked = attempt.fail(now);
        assertTrue(blocked.isBlocked(now));
    }

    @Test
    public void testResetAfterBlockExpires() {
        long now = System.currentTimeMillis();
        LoginAttempt attempt = new LoginAttempt();
        for (int count = 0; count < 5; count++) attempt = attempt.fail(now);
        long afterExpiry = now + 5 * 60 * 1000 + 1;
        LoginAttempt reset = attempt.fail(afterExpiry);
        assertFalse(reset.isExhausted());
    }
}