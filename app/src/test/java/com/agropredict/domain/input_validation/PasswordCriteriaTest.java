package com.agropredict.domain.input_validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public final class PasswordCriteriaTest {

    @Test
    public void testAllCriteriaMet() {
        assertTrue(new PasswordCriteria().isValid("Aa1!"));
    }

    @Test
    public void testEmptyInvalid() {
        assertFalse(new PasswordCriteria().isValid(""));
    }

    @Test
    public void testMissingUppercase() {
        assertFalse(new PasswordCriteria().isValid("a1!"));
    }

    @Test
    public void testMissingLowercase() {
        assertFalse(new PasswordCriteria().isValid("A1!"));
    }

    @Test
    public void testMissingDigit() {
        assertFalse(new PasswordCriteria().isValid("Aa!"));
    }

    @Test
    public void testMissingSpecial() {
        assertFalse(new PasswordCriteria().isValid("Aa1"));
    }

    @Test
    public void testOnlyLowercaseInvalid() {
        assertFalse(new PasswordCriteria().isValid("abc"));
    }
}