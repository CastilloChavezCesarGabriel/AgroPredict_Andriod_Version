package com.agropredict.domain.input_validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public final class PasswordValidatorTest {
    private final PasswordValidator validator = new PasswordValidator();

    @Test
    public void testValidPassword() {
        assertTrue(validator.isValid("Passw0rd!"));
    }

    @Test
    public void testValidPasswordMinimumLength() {
        assertTrue(validator.isValid("Aa1!xxxx"));
    }

    @Test
    public void testNullPassword() {
        assertFalse(validator.isValid(null));
    }

    @Test
    public void testEmptyPassword() {
        assertFalse(validator.isValid(""));
    }

    @Test
    public void testTooShort() {
        assertFalse(validator.isValid("Aa1!xx"));
    }

    @Test
    public void testExactlySevenCharacters() {
        assertFalse(validator.isValid("Aa1!xxx"));
    }

    @Test
    public void testMissingUppercase() {
        assertFalse(validator.isValid("passw0rd!"));
    }

    @Test
    public void testMissingLowercase() {
        assertFalse(validator.isValid("PASSW0RD!"));
    }

    @Test
    public void testMissingDigit() {
        assertFalse(validator.isValid("Password!"));
    }

    @Test
    public void testMissingSpecialCharacter() {
        assertFalse(validator.isValid("Passw0rd1"));
    }

    @Test
    public void testOnlyDigits() {
        assertFalse(validator.isValid("12345678"));
    }

    @Test
    public void testOnlyLowercase() {
        assertFalse(validator.isValid("abcdefgh"));
    }

    @Test
    public void testLongValidPassword() {
        assertTrue(validator.isValid("MyV3ryL0ng&SecureP@ssw0rd!"));
    }

    @Test
    public void testSpecialCharactersVariety() {
        assertTrue(validator.isValid("Aa1@xxxx"));
        assertTrue(validator.isValid("Aa1#xxxx"));
        assertTrue(validator.isValid("Aa1$xxxx"));
        assertTrue(validator.isValid("Aa1%xxxx"));
    }
}