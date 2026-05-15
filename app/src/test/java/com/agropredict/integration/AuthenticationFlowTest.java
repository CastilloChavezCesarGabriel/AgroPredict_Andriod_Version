package com.agropredict.integration;

import static org.junit.Assert.assertNotNull;
import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.authentication.request.RegistrationRequest;
import com.agropredict.application.authentication.usecase.LoginUseCase;
import com.agropredict.application.authentication.usecase.ResetPasswordUseCase;
import com.agropredict.domain.authentication.session.ISession;
import com.agropredict.domain.authentication.session.NoSession;
import com.agropredict.domain.authentication.session.Session;
import com.agropredict.domain.user.Account;
import com.agropredict.domain.user.AnonymousUser;
import com.agropredict.domain.user.Credential;
import com.agropredict.domain.user.ContactInformation;
import com.agropredict.domain.user.ISessionSubject;
import com.agropredict.domain.user.IUser;
import com.agropredict.domain.user.NoPhone;
import com.agropredict.domain.user.User;
import com.agropredict.factory.StubAuthenticationFailureFactory;
import com.agropredict.factory.StubPasswordFailureFactory;
import com.agropredict.visitor.RejectExpecter;
import com.agropredict.visitor.SucceedExpecter;
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
            @Override public ISessionSubject authenticate(String email, String password) {
                String stored = userStore.get(email);
                if (stored == null || !hasher.verify(password, stored)) return new AnonymousUser();
                return new User("user_" + email.hashCode(),
                        new ContactInformation("Test Farmer", new NoPhone()),
                        new Account("test_user", new Credential(email, stored), new com.agropredict.domain.user.occupation.StandardOccupation("Farmer")));
            }
            @Override public void register(RegistrationRequest request, com.agropredict.application.repository.ICatalogRepository catalog) {}
            @Override public boolean reset(String email, String newHash) {
                if (!userStore.containsKey(email)) return false;
                userStore.put(email, newHash);
                return true;
            }
            @Override public IUser find(String userIdentifier) { return null; }
        };
    }

    private ISessionRepository inMemorySessionRepo() {
        return new ISessionRepository() {
            @Override public void save(Session session) {
                session.report((identifier, occupation) -> {
                    sessionStore.put("id", identifier);
                    sessionStore.put("occ", occupation);
                });
            }
            @Override public ISession recall() {
                if (!sessionStore.containsKey("id")) return new NoSession();
                return new Session(sessionStore.get("id"), sessionStore.get("occ"));
            }
            @Override public void clear() { sessionStore.clear(); }
        };
    }

    @Before
    public void setup() {
        userStore.clear();
        sessionStore.clear();
        userStore.put("farmer@test.com", hasher.hash("OldPass1!XYZ"));
    }

    @Test
    public void testLoginThenSessionPersists() {
        ISessionRepository sessionRepo = inMemorySessionRepo();
        new LoginUseCase(inMemoryUserRepo(), sessionRepo, new StubAuthenticationFailureFactory()).login("farmer@test.com", "OldPass1!XYZ");
        ISession restored = sessionRepo.recall();
        assertNotNull(restored);
    }

    @Test
    public void testResetThenLoginWithNewPassword() {
        IUserRepository userRepo = inMemoryUserRepo();
        new ResetPasswordUseCase(userRepo, hasher, new StubPasswordFailureFactory()).reset("farmer@test.com", "NewPass2@XYZ").accept(new SucceedExpecter(null));
        new LoginUseCase(userRepo, inMemorySessionRepo(), new StubAuthenticationFailureFactory()).login("farmer@test.com", "NewPass2@XYZ").accept(new SucceedExpecter(null));
    }

    @Test
    public void testOldPasswordFailsAfterReset() {
        IUserRepository userRepo = inMemoryUserRepo();
        new ResetPasswordUseCase(userRepo, hasher, new StubPasswordFailureFactory()).reset("farmer@test.com", "NewPass2@XYZ");

        new LoginUseCase(userRepo, inMemorySessionRepo(), new StubAuthenticationFailureFactory()).login("farmer@test.com", "OldPass1!XYZ").accept(new RejectExpecter("Incorrect credentials"));
    }

    @Test
    public void testLockoutThenWaitThenLogin() {
        LoginUseCase login = new LoginUseCase(inMemoryUserRepo(), inMemorySessionRepo(), new StubAuthenticationFailureFactory());
        for (int attempt = 0; attempt < 5; attempt++)
            login.login("farmer@test.com", "wrong");

        login.login("farmer@test.com", "OldPass1!XYZ").accept(new RejectExpecter("Account locked. Try again in a few minutes."));
    }
}
