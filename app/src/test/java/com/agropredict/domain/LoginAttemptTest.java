package com.agropredict.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.agropredict.domain.authentication.ILoginAttempt;
import com.agropredict.domain.authentication.InitialAttempt;

import org.junit.Test;

public final class LoginAttemptTest {
    @Test
    public void testInitialAttemptIsAllowed() {
        ILoginAttempt attempt = new InitialAttempt();
        CapturingLoginGate gate = new CapturingLoginGate();
        attempt.evaluate(System.currentTimeMillis(), gate);
        assertTrue(gate.hasReceived("allow"));
        assertFalse(gate.hasReceived("block"));
        assertFalse(gate.hasReceived("exhaust"));
    }

    @Test
    public void testSingleFailureStillAllows() {
        long now = System.currentTimeMillis();
        ILoginAttempt attempt = new InitialAttempt().fail(now);
        CapturingLoginGate gate = new CapturingLoginGate();
        attempt.evaluate(now, gate);
        assertTrue(gate.hasReceived("allow"));
    }

    @Test
    public void testFourFailuresStillAllows() {
        long now = System.currentTimeMillis();
        ILoginAttempt attempt = new InitialAttempt();
        for (int count = 0; count < 4; count++) attempt = attempt.fail(now);
        CapturingLoginGate gate = new CapturingLoginGate();
        attempt.evaluate(now, gate);
        assertTrue(gate.hasReceived("allow"));
    }

    @Test
    public void testFiveFailuresBlocks() {
        long now = System.currentTimeMillis();
        ILoginAttempt attempt = new InitialAttempt();
        for (int count = 0; count < 5; count++) attempt = attempt.fail(now);
        CapturingLoginGate gate = new CapturingLoginGate();
        attempt.evaluate(now, gate);
        assertTrue(gate.hasReceived("block"));
    }

    @Test
    public void testStillBlockedDuringWindow() {
        long now = System.currentTimeMillis();
        ILoginAttempt attempt = new InitialAttempt();
        for (int count = 0; count < 5; count++) attempt = attempt.fail(now);
        CapturingLoginGate gate = new CapturingLoginGate();
        attempt.evaluate(now + 4 * 60 * 1000, gate);
        assertTrue(gate.hasReceived("block"));
    }

    @Test
    public void testExhaustsAfterBlockExpires() {
        long now = System.currentTimeMillis();
        ILoginAttempt attempt = new InitialAttempt();
        for (int count = 0; count < 5; count++) attempt = attempt.fail(now);
        CapturingLoginGate gate = new CapturingLoginGate();
        attempt.evaluate(now + 5 * 60 * 1000 + 1, gate);
        assertTrue(gate.hasReceived("exhaust"));
    }

    @Test
    public void testSuccessResetsAttempts() {
        long now = System.currentTimeMillis();
        ILoginAttempt attempt = new InitialAttempt();
        for (int count = 0; count < 3; count++) attempt = attempt.fail(now);
        attempt = attempt.succeed();
        CapturingLoginGate gate = new CapturingLoginGate();
        attempt.evaluate(now, gate);
        assertTrue(gate.hasReceived("allow"));
    }

    @Test
    public void testFailWhileBlockedDoesNotIncrement() {
        long now = System.currentTimeMillis();
        ILoginAttempt attempt = new InitialAttempt();
        for (int count = 0; count < 5; count++) attempt = attempt.fail(now);
        ILoginAttempt blocked = attempt.fail(now);
        CapturingLoginGate gate = new CapturingLoginGate();
        blocked.evaluate(now, gate);
        assertTrue(gate.hasReceived("block"));
    }

    @Test
    public void testResetAfterBlockExpires() {
        long now = System.currentTimeMillis();
        ILoginAttempt attempt = new InitialAttempt();
        for (int count = 0; count < 5; count++) attempt = attempt.fail(now);
        long afterExpiry = now + 5 * 60 * 1000 + 1;
        ILoginAttempt reset = attempt.fail(afterExpiry);
        CapturingLoginGate gate = new CapturingLoginGate();
        reset.evaluate(afterExpiry, gate);
        assertTrue(gate.hasReceived("allow"));
    }
}