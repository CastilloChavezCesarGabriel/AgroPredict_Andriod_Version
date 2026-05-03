package com.agropredict.application.usecase.authentication;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.request.user_registration.RegistrationRequest;
import com.agropredict.application.service.IPasswordHasher;
import com.agropredict.domain.Session;
import com.agropredict.visitor.TestOperationResultVisitor;

import org.junit.Test;

public final class ResetPasswordUseCaseTest {

    private IUserRepository fakeUserRepo(boolean registered, boolean resetSuccess) {
        return new IUserRepository() {
            @Override public Session authenticate(String email, String password) { return null; }
            @Override public void register(RegistrationRequest request, com.agropredict.application.repository.ICatalogRepository catalog) {}
            @Override public boolean reset(String email, String hash) { return registered && resetSuccess; }
        };
    }

    private final IPasswordHasher fakeHasher = new IPasswordHasher() {
        @Override public String hash(String password) { return "hashed_" + password; }
        @Override public boolean verify(String password, String stored) { return false; }
    };

    @Test
    public void testSuccessfulReset() {
        TestOperationResultVisitor visitor = new TestOperationResultVisitor();
        new ResetPasswordUseCase(fakeUserRepo(true, true), fakeHasher)
            .reset("user@mail.com", "NewPass1!").accept(visitor);
        assertTrue(visitor.isCompleted());
    }

    @Test
    public void testResetUnregisteredEmail() {
        TestOperationResultVisitor visitor = new TestOperationResultVisitor();
        new ResetPasswordUseCase(fakeUserRepo(false, false), fakeHasher)
            .reset("noone@mail.com", "NewPass1!").accept(visitor);
        assertFalse(visitor.isCompleted());
    }

    @Test
    public void testResetWeakPassword() {
        TestOperationResultVisitor visitor = new TestOperationResultVisitor();
        new ResetPasswordUseCase(fakeUserRepo(true, true), fakeHasher)
            .reset("user@mail.com", "weak").accept(visitor);
        assertFalse(visitor.isCompleted());
    }

    @Test
    public void testResetPasswordMissingUppercase() {
        TestOperationResultVisitor visitor = new TestOperationResultVisitor();
        new ResetPasswordUseCase(fakeUserRepo(true, true), fakeHasher)
            .reset("user@mail.com", "passw0rd!").accept(visitor);
        assertFalse(visitor.isCompleted());
    }

    @Test
    public void testResetDatabaseFailure() {
        TestOperationResultVisitor visitor = new TestOperationResultVisitor();
        new ResetPasswordUseCase(fakeUserRepo(true, false), fakeHasher)
            .reset("user@mail.com", "NewPass1!").accept(visitor);
        assertFalse(visitor.isCompleted());
    }
}