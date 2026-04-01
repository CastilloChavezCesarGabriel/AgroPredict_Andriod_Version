package com.agropredict.domain.input_validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public final class UsernameValidatorTest {
    private final UsernameValidator validator = new UsernameValidator();

    @Test
    public void testValidUsername() {
        assertTrue(validator.isValid("JuanPerez"));
    }

    @Test
    public void testValidUsernameWithUnderscore() {
        assertTrue(validator.isValid("juan_perez"));
    }

    @Test
    public void testValidUsernameWithNumbers() {
        assertTrue(validator.isValid("user123"));
    }

    @Test
    public void testMinimumLength() {
        assertTrue(validator.isValid("abcde"));
    }

    @Test
    public void testMaximumLength() {
        assertTrue(validator.isValid("a".repeat(32)));
    }

    @Test
    public void testNullUsername() {
        assertFalse(validator.isValid(null));
    }

    @Test
    public void testEmptyUsername() {
        assertFalse(validator.isValid(""));
    }

    @Test
    public void testTooShort() {
        assertFalse(validator.isValid("abc"));
    }

    @Test
    public void testFourCharacters() {
        assertFalse(validator.isValid("abcd"));
    }

    @Test
    public void testTooLong() {
        assertFalse(validator.isValid("a".repeat(33)));
    }

    @Test
    public void testOnlyNumbers() {
        assertFalse(validator.isValid("12345"));
    }

    @Test
    public void testOnlyUnderscores() {
        assertFalse(validator.isValid("_____"));
    }

    @Test
    public void testSpecialCharacters() {
        assertFalse(validator.isValid("user@name"));
    }

    @Test
    public void testSpaces() {
        assertFalse(validator.isValid("user name"));
    }

    @Test
    public void testHyphen() {
        assertFalse(validator.isValid("user-name"));
    }

    @Test
    public void testNumbersWithOneLetter() {
        assertTrue(validator.isValid("1234a"));
    }
}
