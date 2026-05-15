package com.agropredict.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.agropredict.domain.authentication.attempt.ILoginAttempt;
import com.agropredict.domain.authentication.attempt.InitialAttempt;
import com.agropredict.domain.authentication.gate.LoginGate;
import com.agropredict.domain.authentication.gate.LoginRejectedException;

import org.junit.Test;

public final class LoginAttemptTest {
    @Test
    public void testInitialAttemptIsAllowed() {
        ILoginAttempt attempt = new InitialAttempt();
        assertEquals("allow", evaluate(attempt, System.currentTimeMillis()));
    }

    @Test
    public void testSingleFailureStillAllows() {
        long now = System.currentTimeMillis();
        ILoginAttempt attempt = new InitialAttempt().fail(now);
        assertEquals("allow", evaluate(attempt, now));
    }

    @Test
    public void testFourFailuresStillAllows() {
        long now = System.currentTimeMillis();
        ILoginAttempt attempt = new InitialAttempt();
        for (int count = 0; count < 4; count++) attempt = attempt.fail(now);
        assertEquals("allow", evaluate(attempt, now));
    }

    @Test
    public void testFiveFailuresBlocks() {
        long now = System.currentTimeMillis();
        ILoginAttempt attempt = new InitialAttempt();
        for (int count = 0; count < 5; count++) attempt = attempt.fail(now);
        assertEquals("blocked", evaluate(attempt, now));
    }

    @Test
    public void testStillBlockedDuringWindow() {
        long now = System.currentTimeMillis();
        ILoginAttempt attempt = new InitialAttempt();
        for (int count = 0; count < 5; count++) attempt = attempt.fail(now);
        assertEquals("blocked", evaluate(attempt, now + 4 * 60 * 1000));
    }

    @Test
    public void testExhaustsAfterBlockExpires() {
        long now = System.currentTimeMillis();
        ILoginAttempt attempt = new InitialAttempt();
        for (int count = 0; count < 5; count++) attempt = attempt.fail(now);
        assertEquals("exhausted", evaluate(attempt, now + 5 * 60 * 1000 + 1));
    }

    @Test
    public void testSuccessResetsAttempts() {
        long now = System.currentTimeMillis();
        ILoginAttempt attempt = new InitialAttempt();
        for (int count = 0; count < 3; count++) attempt = attempt.fail(now);
        attempt = attempt.succeed();
        assertEquals("allow", evaluate(attempt, now));
    }

    @Test
    public void testFailWhileBlockedDoesNotIncrement() {
        long now = System.currentTimeMillis();
        ILoginAttempt attempt = new InitialAttempt();
        for (int count = 0; count < 5; count++) attempt = attempt.fail(now);
        ILoginAttempt blocked = attempt.fail(now);
        assertEquals("blocked", evaluate(blocked, now));
    }

    @Test
    public void testResetAfterBlockExpires() {
        long now = System.currentTimeMillis();
        ILoginAttempt attempt = new InitialAttempt();
        for (int count = 0; count < 5; count++) attempt = attempt.fail(now);
        long afterExpiry = now + 5 * 60 * 1000 + 1;
        ILoginAttempt reset = attempt.fail(afterExpiry);
        assertEquals("allow", evaluate(reset, afterExpiry));
        assertNotEquals("blocked", evaluate(reset, afterExpiry));
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
