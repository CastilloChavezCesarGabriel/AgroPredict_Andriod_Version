package com.agropredict.domain.input_validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public final class EmailValidatorTest {
    private final EmailValidator validator = new EmailValidator();

    @Test
    public void testValidEmail() {
        assertTrue(validator.isValid("user@example.com"));
    }

    @Test
    public void testValidEmailWithDots() {
        assertTrue(validator.isValid("user.name@example.com"));
    }

    @Test
    public void testValidEmailWithHyphens() {
        assertTrue(validator.isValid("user-name@example.com"));
    }

    @Test
    public void testValidEmailWithUnderscore() {
        assertTrue(validator.isValid("user_name@example.com"));
    }

    @Test
    public void testValidEmailWithNumbers() {
        assertTrue(validator.isValid("user123@example.com"));
    }

    @Test
    public void testValidEmailSubdomain() {
        assertTrue(validator.isValid("user@mail.example.com"));
    }

    @Test
    public void testNullEmail() {
        assertFalse(validator.isValid(null));
    }

    @Test
    public void testEmptyEmail() {
        assertFalse(validator.isValid(""));
    }

    @Test
    public void testMissingAtSymbol() {
        assertFalse(validator.isValid("userexample.com"));
    }

    @Test
    public void testMissingDomain() {
        assertFalse(validator.isValid("user@"));
    }

    @Test
    public void testMissingUsername() {
        assertFalse(validator.isValid("@example.com"));
    }

    @Test
    public void testMissingTld() {
        assertFalse(validator.isValid("user@example"));
    }

    @Test
    public void testSingleCharTld() {
        assertFalse(validator.isValid("user@example.c"));
    }

    @Test
    public void testSpacesInEmail() {
        assertFalse(validator.isValid("user @example.com"));
    }

    @Test
    public void testDoubleAt() {
        assertFalse(validator.isValid("user@@example.com"));
    }

    @Test
    public void testStartsWithDot() {
        assertFalse(validator.isValid(".user@example.com"));
    }

    @Test
    public void testStartsWithSpecialChar() {
        assertFalse(validator.isValid("!user@example.com"));
    }
}
