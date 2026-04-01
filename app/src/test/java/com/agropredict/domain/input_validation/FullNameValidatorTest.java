package com.agropredict.domain.input_validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public final class FullNameValidatorTest {
    private final FullNameValidator validator = new FullNameValidator();

    @Test
    public void testValidFullName() {
        assertTrue(validator.isValid("Juan Perez"));
    }

    @Test
    public void testValidSingleName() {
        assertTrue(validator.isValid("Juan"));
    }

    @Test
    public void testValidAccentedName() {
        assertTrue(validator.isValid("Jose Garcia"));
    }

    @Test
    public void testMinimumLength() {
        assertTrue(validator.isValid("Jo"));
    }

    @Test
    public void testMaximumLength() {
        assertTrue(validator.isValid("A".repeat(50)));
    }

    @Test
    public void testNullName() {
        assertFalse(validator.isValid(null));
    }

    @Test
    public void testEmptyName() {
        assertFalse(validator.isValid(""));
    }

    @Test
    public void testSingleCharacter() {
        assertFalse(validator.isValid("J"));
    }

    @Test
    public void testTooLong() {
        assertFalse(validator.isValid("A".repeat(51)));
    }

    @Test
    public void testOnlySpaces() {
        assertFalse(validator.isValid("     "));
    }

    @Test
    public void testContainsNumbers() {
        assertFalse(validator.isValid("Juan123"));
    }

    @Test
    public void testContainsSpecialChars() {
        assertFalse(validator.isValid("Juan@Perez"));
    }

    @Test
    public void testLeadingTrailingSpaces() {
        assertTrue(validator.isValid("  Juan Perez  "));
    }
}
