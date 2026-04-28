package com.agropredict.domain.input_validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public final class PasswordConfirmationTest {
    private final PasswordConfirmation confirmation = new PasswordConfirmation();

    @Test
    public void testMatch() {
        assertTrue(confirmation.confirms("Passw0rd!", "Passw0rd!"));
    }

    @Test
    public void testMismatch() {
        assertFalse(confirmation.confirms("Passw0rd!", "Different1!"));
    }

    @Test
    @SuppressWarnings("ConstantValue")
    public void testNullPassword() {
        assertFalse(confirmation.confirms(null, "Passw0rd!"));
    }

    @Test
    public void testNullConfirmation() {
        assertFalse(confirmation.confirms("Passw0rd!", null));
    }
}