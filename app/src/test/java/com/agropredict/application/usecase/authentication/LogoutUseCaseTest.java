package com.agropredict.application.usecase.authentication;

import static org.junit.Assert.assertTrue;

import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.domain.Session;

import org.junit.Test;

public final class LogoutUseCaseTest {

    @Test
    public void testLogoutClearsSession() {
        boolean[] cleared = {false};
        ISessionRepository repo = new ISessionRepository() {
            @Override public void visit(String id, String occ) {}
            @Override public Session recall() { return null; }
            @Override public void clear() { cleared[0] = true; }
        };
        new LogoutUseCase(repo).logout();
        assertTrue(cleared[0]);
    }
}
