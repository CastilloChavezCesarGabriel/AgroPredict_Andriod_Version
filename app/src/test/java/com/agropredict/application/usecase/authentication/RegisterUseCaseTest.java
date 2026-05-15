package com.agropredict.application.usecase.authentication;

import com.agropredict.application.authentication.usecase.RegisterUseCase;
import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.authentication.request.CredentialDraft;
import com.agropredict.application.authentication.request.CredentialFailureContext;
import com.agropredict.application.authentication.request.Registration;
import com.agropredict.application.authentication.request.RegistrationRequest;
import com.agropredict.application.authentication.request.Profile;
import com.agropredict.application.authentication.request.Registrant;
import com.agropredict.application.authentication.request.RegistrantFailureContext;
import com.agropredict.factory.StubEmailFailureFactory;
import com.agropredict.factory.StubFullNameFailureFactory;
import com.agropredict.factory.StubPasswordFailureFactory;
import com.agropredict.factory.StubPhoneNumberFailureFactory;
import com.agropredict.factory.StubUsernameFailureFactory;
import com.agropredict.repository.FixedCatalogRepository;
import com.agropredict.repository.ScriptedUserRepository;
import com.agropredict.visitor.RejectExpecter;
import com.agropredict.visitor.SucceedExpecter;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public final class RegisterUseCaseTest {
    private final RegistrantFailureContext registrantFailures = new RegistrantFailureContext(
            new StubFullNameFailureFactory(), new StubPhoneNumberFailureFactory());
    private final CredentialFailureContext credentialFailures = new CredentialFailureContext(
            new StubEmailFailureFactory(), new StubPasswordFailureFactory());
    private final StubUsernameFailureFactory usernameFailures = new StubUsernameFailureFactory();

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
        return compose("Juan Perez", "3312345678", "juan@mail.com", "Passw0rd!XYZ", "juanperez");
    }

    private RegistrationRequest compose(String fullName, String phone, String email, String password, String username) {
        return RegistrationRequest.compose(new Registration(
            new Registrant(fullName, phone, registrantFailures),
            new CredentialDraft(email, password, credentialFailures),
            new Profile(username, "Farmer", usernameFailures)
        ));
    }

    @Test
    public void testSuccessfulRegistration() {
        new RegisterUseCase(accept(), arrange()).register(compose()).accept(new SucceedExpecter(null));
    }

    @Test
    public void testDuplicateEmailRejected() {
        new RegisterUseCase(reject("This email is already registered"), arrange())
            .register(compose()).accept(new RejectExpecter("This email is already registered"));
    }

    @Test
    public void testDuplicateUsernameRejected() {
        new RegisterUseCase(reject("This username already exists"), arrange())
            .register(compose()).accept(new RejectExpecter("This username already exists"));
    }

    @Test
    public void testInvalidEmailRejected() {
        RegistrationRequest request = compose("Juan Perez", "3312345678", "not-an-email", "Passw0rd!XYZ", "juanperez");
        new RegisterUseCase(accept(), arrange()).register(request).accept(new RejectExpecter(null));
    }

    @Test
    public void testInvalidPasswordRejected() {
        RegistrationRequest request = compose("Juan Perez", "3312345678", "juan@mail.com", "weak", "juanperez");
        new RegisterUseCase(accept(), arrange()).register(request).accept(new RejectExpecter(null));
    }

    @Test
    public void testInvalidUsernameRejected() {
        RegistrationRequest request = compose("Juan Perez", "3312345678", "juan@mail.com", "Passw0rd!XYZ", "ab");
        new RegisterUseCase(accept(), arrange()).register(request).accept(new RejectExpecter(null));
    }

    @Test
    public void testInvalidFullNameRejected() {
        RegistrationRequest request = compose("", "3312345678", "juan@mail.com", "Passw0rd!XYZ", "juanperez");
        new RegisterUseCase(accept(), arrange()).register(request).accept(new RejectExpecter(null));
    }

    @Test
    public void testEmptyValidatedFieldsRejected() {
        RegistrationRequest request = compose("", "", "", "", "minuser");
        new RegisterUseCase(accept(), arrange()).register(request).accept(new RejectExpecter(null));
    }
}