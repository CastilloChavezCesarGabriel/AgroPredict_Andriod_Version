package com.agropredict.domain.input_validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.agropredict.domain.input_validation.requirement.DigitRequirement;
import com.agropredict.domain.input_validation.requirement.ICharacterRequirement;
import com.agropredict.domain.input_validation.requirement.LowercaseRequirement;
import com.agropredict.domain.input_validation.requirement.PasswordPolicy;
import com.agropredict.domain.input_validation.requirement.SpecialCharacterRequirement;
import com.agropredict.domain.input_validation.requirement.UppercaseRequirement;

import org.junit.Test;
import java.util.List;

public final class PasswordPolicyTest {
    private static final List<ICharacterRequirement> REQUIREMENTS = List.of(
            new UppercaseRequirement(),
            new LowercaseRequirement(),
            new DigitRequirement(),
            new SpecialCharacterRequirement());

    @Test
    public void testAllCriteriaMet() {
        assertTrue(new PasswordPolicy(REQUIREMENTS).accepts("Aa1!"));
    }

    @Test
    public void testEmptyInvalid() {
        assertFalse(new PasswordPolicy(REQUIREMENTS).accepts(""));
    }

    @Test
    public void testMissingUppercase() {
        assertFalse(new PasswordPolicy(REQUIREMENTS).accepts("a1!"));
    }

    @Test
    public void testMissingLowercase() {
        assertFalse(new PasswordPolicy(REQUIREMENTS).accepts("A1!"));
    }

    @Test
    public void testMissingDigit() {
        assertFalse(new PasswordPolicy(REQUIREMENTS).accepts("Aa!"));
    }

    @Test
    public void testMissingSpecial() {
        assertFalse(new PasswordPolicy(REQUIREMENTS).accepts("Aa1"));
    }

    @Test
    public void testOnlyLowercaseInvalid() {
        assertFalse(new PasswordPolicy(REQUIREMENTS).accepts("abc"));
    }
}
