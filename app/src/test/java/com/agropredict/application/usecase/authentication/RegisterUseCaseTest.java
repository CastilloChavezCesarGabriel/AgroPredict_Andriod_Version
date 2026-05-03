package com.agropredict.application.usecase.authentication;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.request.user_registration.RegistrationRequest;
import com.agropredict.application.request.user_registration.Account;
import com.agropredict.application.request.user_registration.Authentication;
import com.agropredict.application.request.user_registration.Profile;
import com.agropredict.application.request.user_registration.Registrant;
import com.agropredict.repository.FixedCatalogRepository;
import com.agropredict.repository.ScriptedUserRepository;
import com.agropredict.visitor.TestRegistrationResultVisitor;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public final class RegisterUseCaseTest {
    private ICatalogRepository arrange() {
        Map<String, String> entries = new HashMap<>();
        entries.put("Farmer", "occupation_farmer");
        return new FixedCatalogRepository(entries);
    }

    private IUserRepository accept() {
        return new ScriptedUserRepository(null);
    }

    private IUserRepository reject(String message) {
        return new ScriptedUserRepository(message);
    }

    private RegistrationRequest compose() {
        return new RegistrationRequest(
            new Registrant("Juan Perez", "3312345678"),
            new Account(new Authentication("juan@mail.com", "Passw0rd!"), new Profile("juanperez", "Farmer"))
        );
    }

    @Test
    public void testSuccessfulRegistration() {
        TestRegistrationResultVisitor visitor = new TestRegistrationResultVisitor();
        new RegisterUseCase(accept(), arrange()).register(compose()).accept(visitor);
        assertTrue(visitor.isCompleted());
    }

    @Test
    public void testDuplicateEmailRejected() {
        TestRegistrationResultVisitor visitor = new TestRegistrationResultVisitor();
        new RegisterUseCase(reject("This email is already registered"), arrange())
            .register(compose()).accept(visitor);
        assertFalse(visitor.isCompleted());
        assertTrue(visitor.isRejected("email"));
    }

    @Test
    public void testDuplicateUsernameRejected() {
        TestRegistrationResultVisitor visitor = new TestRegistrationResultVisitor();
        new RegisterUseCase(reject("This username already exists"), arrange())
            .register(compose()).accept(visitor);
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
        new RegisterUseCase(accept(), arrange()).register(request).accept(visitor);
        assertFalse(visitor.isCompleted());
    }

    @Test
    public void testInvalidPasswordRejected() {
        TestRegistrationResultVisitor visitor = new TestRegistrationResultVisitor();
        RegistrationRequest request = new RegistrationRequest(
            new Registrant("Juan Perez", "3312345678"),
            new Account(new Authentication("juan@mail.com", "weak"), new Profile("juanperez", "Farmer"))
        );
        new RegisterUseCase(accept(), arrange()).register(request).accept(visitor);
        assertFalse(visitor.isCompleted());
    }

    @Test
    public void testInvalidUsernameRejected() {
        TestRegistrationResultVisitor visitor = new TestRegistrationResultVisitor();
        RegistrationRequest request = new RegistrationRequest(
            new Registrant("Juan Perez", "3312345678"),
            new Account(new Authentication("juan@mail.com", "Passw0rd!"), new Profile("ab", "Farmer"))
        );
        new RegisterUseCase(accept(), arrange()).register(request).accept(visitor);
        assertFalse(visitor.isCompleted());
    }

    @Test
    public void testInvalidFullNameRejected() {
        TestRegistrationResultVisitor visitor = new TestRegistrationResultVisitor();
        RegistrationRequest request = new RegistrationRequest(
            new Registrant("", "3312345678"),
            new Account(new Authentication("juan@mail.com", "Passw0rd!"), new Profile("juanperez", "Farmer"))
        );
        new RegisterUseCase(accept(), arrange()).register(request).accept(visitor);
        assertFalse(visitor.isCompleted());
    }

    @Test
    public void testEmptyFieldsRejected() {
        TestRegistrationResultVisitor visitor = new TestRegistrationResultVisitor();
        RegistrationRequest request = new RegistrationRequest(
            new Registrant("", ""),
            new Account(new Authentication("", ""), new Profile("", ""))
        );
        new RegisterUseCase(accept(), arrange()).register(request).accept(visitor);
        assertFalse(visitor.isCompleted());
    }
}
