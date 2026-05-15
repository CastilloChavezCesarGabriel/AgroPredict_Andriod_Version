package com.agropredict.application.usecase.authentication;

import static org.junit.Assert.assertTrue;

import com.agropredict.application.authentication.usecase.LogoutUseCase;
import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.domain.authentication.session.ISession;
import com.agropredict.domain.authentication.session.NoSession;
import com.agropredict.domain.authentication.session.Session;

import org.junit.Test;

public final class LogoutUseCaseTest {
    @Test
    public void testLogoutClearsSession() {
        boolean[] cleared = {false};
        ISessionRepository repo = new ISessionRepository() {
            @Override public void save(Session session) {}
            @Override public ISession recall() { return new NoSession(); }
            @Override public void clear() { cleared[0] = true; }
        };
        new LogoutUseCase(repo).logout();
        assertTrue(cleared[0]);
    }
}