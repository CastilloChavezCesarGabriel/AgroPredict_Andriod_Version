package com.agropredict.domain.input_validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public final class PhoneNumberValidatorTest {
    private final PhoneNumberValidator validator = new PhoneNumberValidator();

    @Test
    public void testValidPhone() {
        assertTrue(validator.isValid("3312345678"));
    }

    @Test
    public void testValidLongPhone() {
        assertTrue(validator.isValid("523312345678"));
    }

    @Test
    public void testMinimumLength() {
        assertTrue(validator.isValid("1234567"));
    }

    @Test
    public void testMaximumLength() {
        assertTrue(validator.isValid("123456789012345"));
    }

    @Test
    public void testNullPhone() {
        assertTrue(validator.isValid(null));
    }

    @Test
    public void testEmptyPhone() {
        assertTrue(validator.isValid(""));
    }

    @Test
    public void testTooShort() {
        assertFalse(validator.isValid("123456"));
    }

    @Test
    public void testTooLong() {
        assertFalse(validator.isValid("1234567890123456"));
    }

    @Test
    public void testContainsLetters() {
        assertFalse(validator.isValid("33abc12345"));
    }

    @Test
    public void testContainsDashes() {
        assertFalse(validator.isValid("33-1234-5678"));
    }

    @Test
    public void testContainsSpaces() {
        assertFalse(validator.isValid("33 1234 5678"));
    }

    @Test
    public void testContainsPlus() {
        assertFalse(validator.isValid("+523312345678"));
    }
}
