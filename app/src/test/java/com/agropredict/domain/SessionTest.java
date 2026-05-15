package com.agropredict.domain;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.agropredict.domain.authentication.session.Session;
import com.agropredict.visitor.ElevateExpecter;
import com.agropredict.visitor.LimitExpecter;
import com.agropredict.visitor.TestSessionVisitor;
import org.junit.Test;

public final class SessionTest {
    @Test
    public void testNullUserIdentifierRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Session(null, "Farmer"));
    }

    @Test
    public void testEmptyUserIdentifierRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Session("", "Farmer"));
    }

    @Test
    public void testAdvancedAgronomist() {
        new Session("user_123", "Agronomist").observe(new ElevateExpecter());
    }

    @Test
    public void testAdvancedSpecialist() {
        new Session("user_123", "Specialist").observe(new ElevateExpecter());
    }

    @Test
    public void testAdvancedResearcher() {
        new Session("user_123", "Researcher").observe(new ElevateExpecter());
    }

    @Test
    public void testNotAdvancedFarmer() {
        new Session("user_123", "Farmer").observe(new LimitExpecter());
    }

    @Test
    public void testNotAdvancedNull() {
        new Session("user_123", null).observe(new LimitExpecter());
    }

    @Test
    public void testNotAdvancedEmpty() {
        new Session("user_123", "").observe(new LimitExpecter());
    }

    @Test
    public void testNotAdvancedUnknownRole() {
        new Session("user_123", "Student").observe(new LimitExpecter());
    }

    @Test
    public void testAcceptVisitor() {
        TestSessionVisitor visitor = new TestSessionVisitor();
        new Session("user_42", "Farmer").report(visitor);
        assertTrue(visitor.isIdentified("user_42"));
        assertTrue(visitor.isOccupied("Farmer"));
    }
}
