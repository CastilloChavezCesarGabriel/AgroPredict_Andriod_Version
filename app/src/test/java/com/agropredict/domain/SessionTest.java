package com.agropredict.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.agropredict.visitor.TestSessionVisitor;
import org.junit.Test;

public final class SessionTest {
    @Test
    public void testActiveSession() {
        assertTrue(new Session("user_123", "Farmer").isActive());
    }

    @Test
    public void testInactiveSessionNull() {
        assertFalse(new Session(null, "Farmer").isActive());
    }

    @Test
    public void testInactiveSessionEmpty() {
        assertFalse(new Session("", "Farmer").isActive());
    }

    @Test
    public void testAdvancedAgronomist() {
        assertTrue(new Session("user_123", "Agronomist").isAdvanced());
    }

    @Test
    public void testAdvancedSpecialist() {
        assertTrue(new Session("user_123", "Specialist").isAdvanced());
    }

    @Test
    public void testAdvancedResearcher() {
        assertTrue(new Session("user_123", "Researcher").isAdvanced());
    }

    @Test
    public void testNotAdvancedFarmer() {
        assertFalse(new Session("user_123", "Farmer").isAdvanced());
    }

    @Test
    public void testNotAdvancedNull() {
        assertFalse(new Session("user_123", null).isAdvanced());
    }

    @Test
    public void testNotAdvancedEmpty() {
        assertFalse(new Session("user_123", "").isAdvanced());
    }

    @Test
    public void testNotAdvancedUnknownRole() {
        assertFalse(new Session("user_123", "Student").isAdvanced());
    }

    @Test
    public void testAcceptVisitor() {
        TestSessionVisitor visitor = new TestSessionVisitor();
        new Session("user_42", "Farmer").accept(visitor);
        assertTrue(visitor.isIdentified("user_42"));
        assertTrue(visitor.isOccupied("Farmer"));
    }
}