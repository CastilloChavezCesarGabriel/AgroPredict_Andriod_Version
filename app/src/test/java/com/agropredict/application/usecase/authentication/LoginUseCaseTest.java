package com.agropredict.application.usecase.authentication;

import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.request.user_registration.RegistrationRequest;
import com.agropredict.domain.authentication.ISession;
import com.agropredict.domain.authentication.NoSession;
import com.agropredict.domain.authentication.Session;
import com.agropredict.domain.user.Account;
import com.agropredict.domain.user.AnonymousUser;
import com.agropredict.domain.user.Credential;
import com.agropredict.domain.user.ContactInformation;
import com.agropredict.domain.user.ISessionSubject;
import com.agropredict.domain.user.User;
import com.agropredict.visitor.RejectExpecter;
import com.agropredict.visitor.SucceedExpecter;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public final class LoginUseCaseTest {
    private final User farmer = new User("user_1",
            new ContactInformation("Test Name", null),
            new Account("test_user", new Credential("test@mail.com", "hash"), "Farmer"));

    private IUserRepository fakeUserRepo(ISessionSubject returnSubject) {
        return new IUserRepository() {
            @Override public ISessionSubject authenticate(String email, String password) { return returnSubject; }
            @Override public void register(RegistrationRequest request, com.agropredict.application.repository.ICatalogRepository catalog) {}
            @Override public boolean reset(String email, String hash) { return false; }
            @Override public User find(String userIdentifier) { return null; }
        };
    }

    private ISessionRepository fakeSessionRepo() {
        return new ISessionRepository() {
            @Override public void save(Session session) {}
            @Override public ISession recall() { return new NoSession(); }
            @Override public void clear() {}
        };
    }

    @Test
    public void testSuccessfulLogin() {
        LoginUseCase useCase = new LoginUseCase(fakeUserRepo(farmer), fakeSessionRepo());
        useCase.login("test@mail.com", "Passw0rd!XYZ").accept(new SucceedExpecter(null));
    }

    @Test
    public void testFailedLogin() {
        LoginUseCase useCase = new LoginUseCase(fakeUserRepo(new AnonymousUser()), fakeSessionRepo());
        useCase.login("wrong@mail.com", "wrong").accept(new RejectExpecter("Incorrect credentials"));
    }

    @Test
    public void testAccountLockAfterFiveFailures() {
        LoginUseCase useCase = new LoginUseCase(fakeUserRepo(new AnonymousUser()), fakeSessionRepo());
        for (int attempt = 0; attempt < 5; attempt++)
            useCase.login("bad@mail.com", "wrong");
        useCase.login("bad@mail.com", "wrong").accept(new RejectExpecter("Account locked. Try again in a few minutes."));
    }

    @Test
    public void testSuccessResetsAttempts() {
        IUserRepository conditional = new IUserRepository() {
            private int callCount = 0;
            @Override public ISessionSubject authenticate(String email, String password) {
                return ++callCount <= 3 ? new AnonymousUser() : farmer;
            }
            @Override public void register(RegistrationRequest request, com.agropredict.application.repository.ICatalogRepository catalog) {}
            @Override public boolean reset(String email, String hash) { return false; }
            @Override public User find(String userIdentifier) { return null; }
        };
        LoginUseCase useCase = new LoginUseCase(conditional, fakeSessionRepo());
        useCase.login("x@m.com", "w");
        useCase.login("x@m.com", "w");
        useCase.login("x@m.com", "w");
        useCase.login("x@m.com", "correct").accept(new SucceedExpecter(null));
    }

    @Test
    public void testSessionSavedOnSuccess() {
        boolean[] saved = {false};
        ISessionRepository trackingSession = new ISessionRepository() {
            @Override public void save(Session session) { saved[0] = true; }
            @Override public ISession recall() { return new NoSession(); }
            @Override public void clear() {}
        };
        new LoginUseCase(fakeUserRepo(farmer), trackingSession)
            .login("test@mail.com", "Passw0rd!XYZ");
        assertTrue(saved[0]);
    }
}
