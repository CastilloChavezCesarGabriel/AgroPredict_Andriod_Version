package com.agropredict.domain.identifier;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public final class IdentifierFactoryTest {
    @Test
    public void testGenerateWithPrefix() {
        String identifier = IdentifierFactory.generate("user");
        assertTrue(identifier.startsWith("user_"));
    }

    @Test
    public void testGenerateNotNull() {
        assertNotNull(IdentifierFactory.generate("crop"));
    }

    @Test
    public void testGenerateUniqueness() {
        String first = IdentifierFactory.generate("test");
        String second = IdentifierFactory.generate("test");
        assertNotEquals(first, second);
    }

    @Test
    public void testGenerateDifferentPrefixes() {
        String userIdentifier = IdentifierFactory.generate("user");
        String cropIdentifier = IdentifierFactory.generate("crop");
        assertTrue(userIdentifier.startsWith("user_"));
        assertTrue(cropIdentifier.startsWith("crop_"));
    }
}