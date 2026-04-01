package com.agropredict.domain;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public final class IdentifierTest {
    @Test
    public void testGenerateWithPrefix() {
        String identifier = Identifier.generate("user");
        assertTrue(identifier.startsWith("user_"));
    }

    @Test
    public void testGenerateNotNull() {
        assertNotNull(Identifier.generate("crop"));
    }

    @Test
    public void testGenerateUniqueness() {
        String first = Identifier.generate("test");
        String second = Identifier.generate("test");
        assertNotEquals(first, second);
    }

    @Test
    public void testGenerateDifferentPrefixes() {
        String userIdentifier = Identifier.generate("user");
        String cropIdentifier = Identifier.generate("crop");
        assertTrue(userIdentifier.startsWith("user_"));
        assertTrue(cropIdentifier.startsWith("crop_"));
    }
}