package com.agropredict.application.usecase.authentication;

import static org.junit.Assert.assertTrue;

import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.domain.Session;
import com.agropredict.visitor.TestSessionVisitor;

import org.junit.Test;

public final class CheckSessionUseCaseTest {
    private ISessionRepository fakeSessionRepository(Session session) {
        return new ISessionRepository() {
            @Override public void save(Session toPersist) {}
            @Override public Session recall() { return session; }
            @Override public void clear() {}
        };
    }

    @Test
    public void testCheckActiveSession() {
        TestSessionVisitor visitor = new TestSessionVisitor();
        new CheckSessionUseCase(fakeSessionRepository(new Session("user_1", "Farmer"))).check(visitor);
        assertTrue(visitor.isIdentified("user_1"));
    }

    @Test
    public void testCheckSessionReturnsOccupation() {
        TestSessionVisitor visitor = new TestSessionVisitor();
        new CheckSessionUseCase(fakeSessionRepository(new Session("user_1", "Agronomist"))).check(visitor);
        assertTrue(visitor.isOccupied("Agronomist"));
    }
}