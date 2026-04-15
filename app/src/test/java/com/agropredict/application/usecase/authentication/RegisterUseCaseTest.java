package com.agropredict.application.usecase.authentication;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.request.user_registration.RegistrationException;
import com.agropredict.application.request.user_registration.RegistrationRequest;
import com.agropredict.application.request.user_registration.Account;
import com.agropredict.application.request.user_registration.Authentication;
import com.agropredict.application.request.user_registration.Profile;
import com.agropredict.application.request.user_registration.Registrant;
import com.agropredict.application.service.IPasswordHasher;
import com.agropredict.domain.Session;
import com.agropredict.visitor.TestRegistrationResultVisitor;

import org.junit.Test;

public final class RegisterUseCaseTest {

    private IUserRepository fakeUserRepo(String rejectionMessage) {
        return new IUserRepository() {
            @Override public Session authenticate(String email, String password) { return null; }
            @Override public void register(RegistrationRequest request, IPasswordHasher hasher) {
                if (rejectionMessage != null) throw new RegistrationException(rejectionMessage);
            }
            @Override public boolean reset(String email, String hash) { return false; }
        };
    }

    private final IPasswordHasher fakeHasher = new IPasswordHasher() {
        @Override public String hash(String password) { return "hashed_" + password; }
        @Override public boolean verify(String password, String stored) { return false; }
    };

    private RegistrationRequest validRequest() {
        return new RegistrationRequest(
            new Registrant("Juan Perez", "3312345678"),
            new Account(new Authentication("juan@mail.com", "Passw0rd!"), new Profile("juanperez", "Farmer"))
        );
    }

    @Test
    public void testSuccessfulRegistration() {
        TestRegistrationResultVisitor visitor = new TestRegistrationResultVisitor();
        new RegisterUseCase(fakeUserRepo(null), fakeHasher)
            .register(validRequest()).accept(visitor);
        assertTrue(visitor.isCompleted());
    }

    @Test
    public void testDuplicateEmailRejected() {
        TestRegistrationResultVisitor visitor = new TestRegistrationResultVisitor();
        new RegisterUseCase(fakeUserRepo("This email is already registered"), fakeHasher)
            .register(validRequest()).accept(visitor);
        assertFalse(visitor.isCompleted());
        assertTrue(visitor.isRejected("email"));
    }

    @Test
    public void testDuplicateUsernameRejected() {
        TestRegistrationResultVisitor visitor = new TestRegistrationResultVisitor();
        new RegisterUseCase(fakeUserRepo("This username already exists"), fakeHasher)
            .register(validRequest()).accept(visitor);
        assertFalse(visitor.isCompleted());
        assertTrue(visitor.isRejected("username"));
    }

    @Test
    public void testInvalidEmailRejected() {
        TestRegistrationResultVisitor visitor = new TestRegistrationResultVisitor();
        RegistrationRequest request = new RegistrationRequest(
            new Registrant("Juan Perez", "3312345678"),
            new Account(new Authentication("not-an-email", "Passw0rd!"), new Profile("juanperez", "Farmer"))
        );
        new RegisterUseCase(fakeUserRepo(null), fakeHasher)
            .register(request).accept(visitor);
        assertFalse(visitor.isCompleted());
    }

    @Test
    public void testInvalidPasswordRejected() {
        TestRegistrationResultVisitor visitor = new TestRegistrationResultVisitor();
        RegistrationRequest request = new RegistrationRequest(
            new Registrant("Juan Perez", "3312345678"),
            new Account(new Authentication("juan@mail.com", "weak"), new Profile("juanperez", "Farmer"))
        );
        new RegisterUseCase(fakeUserRepo(null), fakeHasher)
            .register(request).accept(visitor);
        assertFalse(visitor.isCompleted());
    }

    @Test
    public void testInvalidUsernameRejected() {
        TestRegistrationResultVisitor visitor = new TestRegistrationResultVisitor();
        RegistrationRequest request = new RegistrationRequest(
            new Registrant("Juan Perez", "3312345678"),
            new Account(new Authentication("juan@mail.com", "Passw0rd!"), new Profile("ab", "Farmer"))
        );
        new RegisterUseCase(fakeUserRepo(null), fakeHasher)
            .register(request).accept(visitor);
        assertFalse(visitor.isCompleted());
    }

    @Test
    public void testInvalidFullNameRejected() {
        TestRegistrationResultVisitor visitor = new TestRegistrationResultVisitor();
        RegistrationRequest request = new RegistrationRequest(
            new Registrant("", "3312345678"),
            new Account(new Authentication("juan@mail.com", "Passw0rd!"), new Profile("juanperez", "Farmer"))
        );
        new RegisterUseCase(fakeUserRepo(null), fakeHasher)
            .register(request).accept(visitor);
        assertFalse(visitor.isCompleted());
    }

    @Test
    public void testEmptyFieldsRejected() {
        TestRegistrationResultVisitor visitor = new TestRegistrationResultVisitor();
        RegistrationRequest request = new RegistrationRequest(
            new Registrant("", ""),
            new Account(new Authentication("", ""), new Profile("", ""))
        );
        new RegisterUseCase(fakeUserRepo(null), fakeHasher)
            .register(request).accept(visitor);
        assertFalse(visitor.isCompleted());
    }
}