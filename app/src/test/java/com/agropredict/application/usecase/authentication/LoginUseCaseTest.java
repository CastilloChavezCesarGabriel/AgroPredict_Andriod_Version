package com.agropredict.application.usecase.authentication;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.request.user_registration.RegistrationRequest;
import com.agropredict.domain.Session;
import com.agropredict.visitor.TestOperationResultVisitor;
import org.junit.Test;

public final class LoginUseCaseTest {
    private IUserRepository fakeUserRepo(Session returnSession) {
        return new IUserRepository() {
            @Override public Session authenticate(String email, String password) { return returnSession; }
            @Override public void register(RegistrationRequest request, com.agropredict.application.repository.ICatalogRepository catalog) {}
            @Override public boolean reset(String email, String hash) { return false; }
        };
    }

    private ISessionRepository fakeSessionRepo() {
        return new ISessionRepository() {
            @Override public void save(Session session) {}
            @Override public Session recall() { return null; }
            @Override public void clear() {}
        };
    }

    @Test
    public void testSuccessfulLogin() {
        LoginUseCase useCase = new LoginUseCase(fakeUserRepo(new Session("user_1", "Farmer")), fakeSessionRepo());
        TestOperationResultVisitor visitor = new TestOperationResultVisitor();
        useCase.login("test@mail.com", "Passw0rd!").accept(visitor);
        assertTrue(visitor.isCompleted());
    }

    @Test
    public void testFailedLogin() {
        LoginUseCase useCase = new LoginUseCase(fakeUserRepo(null), fakeSessionRepo());
        TestOperationResultVisitor visitor = new TestOperationResultVisitor();
        useCase.login("wrong@mail.com", "wrong").accept(visitor);
        assertFalse(visitor.isCompleted());
    }

    @Test
    public void testAccountLockAfterFiveFailures() {
        LoginUseCase useCase = new LoginUseCase(fakeUserRepo(null), fakeSessionRepo());
        for (int attempt = 0; attempt < 5; attempt++)
            useCase.login("bad@mail.com", "wrong");
        TestOperationResultVisitor visitor = new TestOperationResultVisitor();
        useCase.login("bad@mail.com", "wrong").accept(visitor);
        assertTrue(visitor.isRejected("Account locked. Try again in a few minutes."));
    }

    @Test
    public void testSuccessResetsAttempts() {
        IUserRepository conditional = new IUserRepository() {
            private int callCount = 0;
            @Override public Session authenticate(String email, String password) {
                return ++callCount <= 3 ? null : new Session("user_1", "Farmer");
            }
            @Override public void register(RegistrationRequest request, com.agropredict.application.repository.ICatalogRepository catalog) {}
            @Override public boolean reset(String email, String hash) { return false; }
        };
        LoginUseCase useCase = new LoginUseCase(conditional, fakeSessionRepo());
        useCase.login("x@m.com", "w");
        useCase.login("x@m.com", "w");
        useCase.login("x@m.com", "w");
        TestOperationResultVisitor visitor = new TestOperationResultVisitor();
        useCase.login("x@m.com", "correct").accept(visitor);
        assertTrue(visitor.isCompleted());
    }

    @Test
    public void testSessionSavedOnSuccess() {
        boolean[] saved = {false};
        ISessionRepository trackingSession = new ISessionRepository() {
            @Override public void save(Session session) { saved[0] = true; }
            @Override public Session recall() { return null; }
            @Override public void clear() {}
        };
        new LoginUseCase(fakeUserRepo(new Session("user_1", "Farmer")), trackingSession)
            .login("test@mail.com", "Passw0rd!");
        assertTrue(saved[0]);
    }
}