package com.agropredict.application.usecase.authentication;

import com.agropredict.application.authentication.usecase.RegisterUseCase;
import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.authentication.request.Registration;
import com.agropredict.application.authentication.request.RegistrationRequest;
import com.agropredict.application.authentication.request.Credential;
import com.agropredict.application.authentication.request.Profile;
import com.agropredict.application.authentication.request.Registrant;
import com.agropredict.repository.FixedCatalogRepository;
import com.agropredict.repository.ScriptedUserRepository;
import com.agropredict.visitor.RejectExpecter;
import com.agropredict.visitor.SucceedExpecter;
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
        return RegistrationRequest.compose(new Registration(
            new Registrant("Juan Perez", "3312345678"),
            new Credential("juan@mail.com", "Passw0rd!XYZ"),
            new Profile("juanperez", "Farmer")
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
        RegistrationRequest request = RegistrationRequest.compose(new Registration(
            new Registrant("Juan Perez", "3312345678"),
            new Credential("not-an-email", "Passw0rd!XYZ"),
            new Profile("juanperez", "Farmer")
        ));
        new RegisterUseCase(accept(), arrange()).register(request).accept(new RejectExpecter(null));
    }

    @Test
    public void testInvalidPasswordRejected() {
        RegistrationRequest request = RegistrationRequest.compose(new Registration(
            new Registrant("Juan Perez", "3312345678"),
            new Credential("juan@mail.com", "weak"),
            new Profile("juanperez", "Farmer")
        ));
        new RegisterUseCase(accept(), arrange()).register(request).accept(new RejectExpecter(null));
    }

    @Test
    public void testInvalidUsernameRejected() {
        RegistrationRequest request = RegistrationRequest.compose(new Registration(
            new Registrant("Juan Perez", "3312345678"),
            new Credential("juan@mail.com", "Passw0rd!XYZ"),
            new Profile("ab", "Farmer")
        ));
        new RegisterUseCase(accept(), arrange()).register(request).accept(new RejectExpecter(null));
    }

    @Test
    public void testInvalidFullNameRejected() {
        RegistrationRequest request = RegistrationRequest.compose(new Registration(
            new Registrant("", "3312345678"),
            new Credential("juan@mail.com", "Passw0rd!XYZ"),
            new Profile("juanperez", "Farmer")
        ));
        new RegisterUseCase(accept(), arrange()).register(request).accept(new RejectExpecter(null));
    }

    @Test
    public void testEmptyFieldsRejected() {
        RegistrationRequest request = RegistrationRequest.compose(new Registration(
            new Registrant("", ""),
            new Credential("", ""),
            new Profile("", "")
        ));
        new RegisterUseCase(accept(), arrange()).register(request).accept(new RejectExpecter(null));
    }
}
