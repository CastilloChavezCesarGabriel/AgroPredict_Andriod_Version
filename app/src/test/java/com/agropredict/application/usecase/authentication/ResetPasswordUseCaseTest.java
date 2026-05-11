package com.agropredict.application.usecase.authentication;

import com.agropredict.application.authentication.usecase.ResetPasswordUseCase;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.authentication.request.RegistrationRequest;
import com.agropredict.application.service.IPasswordHasher;
import com.agropredict.domain.user.AnonymousUser;
import com.agropredict.domain.user.ISessionSubject;
import com.agropredict.domain.user.User;
import com.agropredict.visitor.FailExpecter;
import com.agropredict.visitor.SucceedExpecter;

import org.junit.Test;

public final class ResetPasswordUseCaseTest {

    private IUserRepository fakeUserRepo(boolean registered, boolean resetSuccess) {
        return new IUserRepository() {
            @Override public ISessionSubject authenticate(String email, String password) { return new AnonymousUser(); }
            @Override public void register(RegistrationRequest request, com.agropredict.application.repository.ICatalogRepository catalog) {}
            @Override public boolean reset(String email, String hash) { return registered && resetSuccess; }
            @Override public User find(String userIdentifier) { return null; }
        };
    }

    private final IPasswordHasher fakeHasher = new IPasswordHasher() {
        @Override public String hash(String password) { return "hashed_" + password; }
        @Override public boolean verify(String password, String stored) { return false; }
    };

    @Test
    public void testSuccessfulReset() {
        new ResetPasswordUseCase(fakeUserRepo(true, true), fakeHasher)
            .reset("user@mail.com", "NewPass1!XYZ").accept(new SucceedExpecter(null));
    }

    @Test
    public void testResetUnregisteredEmail() {
        new ResetPasswordUseCase(fakeUserRepo(false, false), fakeHasher)
            .reset("noone@mail.com", "NewPass1!XYZ").accept(new FailExpecter());
    }

    @Test
    public void testResetWeakPassword() {
        new ResetPasswordUseCase(fakeUserRepo(true, true), fakeHasher)
            .reset("user@mail.com", "weak").accept(new FailExpecter());
    }

    @Test
    public void testResetPasswordMissingUppercase() {
        new ResetPasswordUseCase(fakeUserRepo(true, true), fakeHasher)
            .reset("user@mail.com", "passw0rd!xyz").accept(new FailExpecter());
    }

    @Test
    public void testResetDatabaseFailure() {
        new ResetPasswordUseCase(fakeUserRepo(true, false), fakeHasher)
            .reset("user@mail.com", "NewPass1!XYZ").accept(new FailExpecter());
    }
}
