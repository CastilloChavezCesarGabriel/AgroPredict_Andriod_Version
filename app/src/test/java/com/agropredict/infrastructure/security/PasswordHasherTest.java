package com.agropredict.infrastructure.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public final class PasswordHasherTest {
    private final PasswordHasher hasher = new PasswordHasher();

    @Test
    public void testHashNotNull() {
        assertNotNull(hasher.hash("Passw0rd!"));
    }

    @Test
    public void testHashContainsDelimiter() {
        String hashed = hasher.hash("Passw0rd!");
        assertTrue(hashed.contains(":"));
    }

    @Test
    public void testHashHasTwoParts() {
        String hashed = hasher.hash("Passw0rd!");
        String[] parts = hashed.split(":");
        assertEquals(2, parts.length);
    }

    @Test
    public void testVerifyCorrectPassword() {
        String hashed = hasher.hash("Passw0rd!");
        assertTrue(hasher.verify("Passw0rd!", hashed));
    }

    @Test
    public void testVerifyWrongPassword() {
        String hashed = hasher.hash("Passw0rd!");
        assertFalse(hasher.verify("WrongPassword1!", hashed));
    }

    @Test
    public void testDifferentHashesForSamePassword() {
        String first = hasher.hash("Passw0rd!");
        String second = hasher.hash("Passw0rd!");
        assertNotEquals(first, second);
    }

    @Test
    public void testVerifyBothHashesValid() {
        String first = hasher.hash("Passw0rd!");
        String second = hasher.hash("Passw0rd!");
        assertTrue(hasher.verify("Passw0rd!", first));
        assertTrue(hasher.verify("Passw0rd!", second));
    }

    @Test
    public void testVerifyMalformedStoredHash() {
        assertFalse(hasher.verify("Passw0rd!", "notahash"));
    }

    @Test
    public void testVerifyEmptyPassword() {
        String hashed = hasher.hash("");
        assertTrue(hasher.verify("", hashed));
        assertFalse(hasher.verify("notempty", hashed));
    }

    @Test
    public void testHashLongPassword() {
        String longPassword = "A".repeat(100) + "a1!";
        String hashed = hasher.hash(longPassword);
        assertTrue(hasher.verify(longPassword, hashed));
    }

    @Test
    public void testHashSpecialCharacters() {
        String special = "P@$$w0rd!#%^&*()";
        String hashed = hasher.hash(special);
        assertTrue(hasher.verify(special, hashed));
    }

    @Test
    public void testTimingAttackResistance() {
        String hashed = hasher.hash("Passw0rd!");
        long startCorrect = System.nanoTime();
        hasher.verify("Passw0rd!", hashed);
        long correctTime = System.nanoTime() - startCorrect;

        long startWrong = System.nanoTime();
        hasher.verify("WrongPwd1!", hashed);
        long wrongTime = System.nanoTime() - startWrong;

        double ratio = (double) correctTime / wrongTime;
        assertTrue("Timing should be similar", ratio > 0.5 && ratio < 2.0);
    }
}
