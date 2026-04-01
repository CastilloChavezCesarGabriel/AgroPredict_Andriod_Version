package com.agropredict.integration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.request.user_registration.RegistrationRequest;
import com.agropredict.application.service.IPasswordHasher;
import com.agropredict.application.usecase.authentication.LoginUseCase;
import com.agropredict.application.usecase.authentication.ResetPasswordUseCase;
import com.agropredict.domain.Session;
import com.agropredict.visitor.TestOperationResultVisitor;
import com.agropredict.infrastructure.security.PasswordHasher;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public final class AuthenticationFlowTest {
    private final PasswordHasher hasher = new PasswordHasher();
    private final Map<String, String> userStore = new HashMap<>();
    private final Map<String, String> sessionStore = new HashMap<>();

    private IUserRepository inMemoryUserRepo() {
        return new IUserRepository() {
            @Override public Session authenticate(String email, String password) {
                String stored = userStore.get(email);
                if (stored == null || !hasher.verify(password, stored)) return null;
                return new Session("user_" + email.hashCode(), "Farmer");
            }
            @Override public void register(RegistrationRequest request, IPasswordHasher hasher) {}
            @Override public boolean isRegistered(String email) { return userStore.containsKey(email); }
            @Override public boolean isTaken(String username) { return false; }
            @Override public boolean reset(String email, String newHash) {
                if (!userStore.containsKey(email)) return false;
                userStore.put(email, newHash);
                return true;
            }
        };
    }

    private ISessionRepository inMemorySessionRepo() {
        return new ISessionRepository() {
            @Override public void visit(String identifier, String occupation) {
                sessionStore.put("id", identifier);
                sessionStore.put("occ", occupation);
            }
            @Override public Session recall() {
                if (!sessionStore.containsKey("id")) return null;
                return new Session(sessionStore.get("id"), sessionStore.get("occ"));
            }
            @Override public void clear() { sessionStore.clear(); }
        };
    }

    @Before
    public void setup() {
        userStore.clear();
        sessionStore.clear();
        userStore.put("farmer@test.com", hasher.hash("OldPass1!"));
    }

    @Test
    public void testLoginThenSessionPersists() {
        ISessionRepository sessionRepo = inMemorySessionRepo();
        new LoginUseCase(inMemoryUserRepo(), sessionRepo).login("farmer@test.com", "OldPass1!");
        Session restored = sessionRepo.recall();
        assertTrue(restored != null && restored.isActive());
    }

    @Test
    public void testResetThenLoginWithNewPassword() {
        IUserRepository userRepo = inMemoryUserRepo();
        TestOperationResultVisitor resetVisitor = new TestOperationResultVisitor();
        new ResetPasswordUseCase(userRepo, hasher).reset("farmer@test.com", "NewPass2@").accept(resetVisitor);
        assertTrue(resetVisitor.isCompleted());

        TestOperationResultVisitor loginVisitor = new TestOperationResultVisitor();
        new LoginUseCase(userRepo, inMemorySessionRepo()).login("farmer@test.com", "NewPass2@").accept(loginVisitor);
        assertTrue(loginVisitor.isCompleted());
    }

    @Test
    public void testOldPasswordFailsAfterReset() {
        IUserRepository userRepo = inMemoryUserRepo();
        new ResetPasswordUseCase(userRepo, hasher).reset("farmer@test.com", "NewPass2@");

        TestOperationResultVisitor visitor = new TestOperationResultVisitor();
        new LoginUseCase(userRepo, inMemorySessionRepo()).login("farmer@test.com", "OldPass1!").accept(visitor);
        assertFalse(visitor.isCompleted());
    }

    @Test
    public void testLockoutThenWaitThenLogin() {
        LoginUseCase login = new LoginUseCase(inMemoryUserRepo(), inMemorySessionRepo());
        for (int attempt = 0; attempt < 5; attempt++)
            login.login("farmer@test.com", "wrong");

        TestOperationResultVisitor visitor = new TestOperationResultVisitor();
        login.login("farmer@test.com", "OldPass1!").accept(visitor);
        assertFalse(visitor.isCompleted());
    }
}
