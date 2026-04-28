package com.agropredict.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.agropredict.visitor.TestOccupationVisitor;
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
        TestOccupationVisitor handler = new TestOccupationVisitor();
        new Session("user_123", "Agronomist").observe(handler);
        assertTrue(handler.sawAdvanced());
    }

    @Test
    public void testAdvancedSpecialist() {
        TestOccupationVisitor handler = new TestOccupationVisitor();
        new Session("user_123", "Specialist").observe(handler);
        assertTrue(handler.sawAdvanced());
    }

    @Test
    public void testAdvancedResearcher() {
        TestOccupationVisitor handler = new TestOccupationVisitor();
        new Session("user_123", "Researcher").observe(handler);
        assertTrue(handler.sawAdvanced());
    }

    @Test
    public void testNotAdvancedFarmer() {
        TestOccupationVisitor handler = new TestOccupationVisitor();
        new Session("user_123", "Farmer").observe(handler);
        assertFalse(handler.sawAdvanced());
        assertTrue(handler.sawBasic());
    }

    @Test
    public void testNotAdvancedNull() {
        TestOccupationVisitor handler = new TestOccupationVisitor();
        new Session("user_123", null).observe(handler);
        assertFalse(handler.sawAdvanced());
        assertTrue(handler.sawBasic());
    }

    @Test
    public void testNotAdvancedEmpty() {
        TestOccupationVisitor handler = new TestOccupationVisitor();
        new Session("user_123", "").observe(handler);
        assertFalse(handler.sawAdvanced());
        assertTrue(handler.sawBasic());
    }

    @Test
    public void testNotAdvancedUnknownRole() {
        TestOccupationVisitor handler = new TestOccupationVisitor();
        new Session("user_123", "Student").observe(handler);
        assertFalse(handler.sawAdvanced());
        assertTrue(handler.sawBasic());
    }

    @Test
    public void testAcceptVisitor() {
        TestSessionVisitor visitor = new TestSessionVisitor();
        new Session("user_42", "Farmer").accept(visitor);
        assertTrue(visitor.isIdentified("user_42"));
        assertTrue(visitor.isOccupied("Farmer"));
    }
}