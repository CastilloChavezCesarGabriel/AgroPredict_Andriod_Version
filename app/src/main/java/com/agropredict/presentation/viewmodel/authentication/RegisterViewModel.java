package com.agropredict.presentation.viewmodel.authentication;

import com.agropredict.application.PasswordHasher;
import com.agropredict.application.result.RegistrationResult;
import com.agropredict.application.usecase.authentication.RegisterUseCase;
import com.agropredict.application.usecase.catalog.ListCatalogUseCase;
import com.agropredict.domain.entity.User;
import com.agropredict.domain.validation.EmailValidator;
import com.agropredict.domain.validation.FullNameValidator;
import com.agropredict.domain.validation.PasswordValidator;
import com.agropredict.domain.validation.PhoneNumberValidator;
import com.agropredict.domain.validation.UsernameValidator;
import com.agropredict.domain.value.user.Credentials;
import com.agropredict.domain.value.user.UserContact;
import com.agropredict.domain.value.user.UserData;
import com.agropredict.domain.value.user.UserIdentity;
import com.agropredict.domain.value.user.UserProfile;
import java.util.Map;
import java.util.Objects;

public final class RegisterViewModel {
    private final RegisterUseCase registerUseCase;
    private final ListCatalogUseCase listOccupationsUseCase;
    private IRegisterView view;

    public RegisterViewModel(RegisterUseCase registerUseCase, ListCatalogUseCase listOccupationsUseCase) {
        this.registerUseCase = registerUseCase;
        this.listOccupationsUseCase = listOccupationsUseCase;
    }

    public void bind(IRegisterView view) {
        this.view = view;
    }

    public void populate() {
        if (view != null) {
            view.populateOccupations(listOccupationsUseCase.list());
        }
    }

    public void register(Map<String, String> formData) {
        String error = validate(formData);
        if (error != null) {
            if (view != null) view.notify(error);
            return;
        }
        submit(formData);
    }

    private void submit(Map<String, String> formData) {
        User user = build(formData);
        RegistrationResult result = registerUseCase.register(user);
        if (view != null) {
            result.accept(new RegistrationResultStrategy(view));
        }
    }

    private String validate(Map<String, String> formData) {
        if (!new FullNameValidator().validate(formData.get("full_name")))
            return "Nombre completo invalido";
        if (!new EmailValidator().validate(formData.get("email")))
            return "Correo electronico invalido";
        if (!new UsernameValidator().validate(formData.get("username")))
            return "Nombre de usuario invalido";
        if (!new PasswordValidator().validate(formData.get("password")))
            return "Contrasena invalida (minimo 8 caracteres, mayuscula, minuscula, numero y caracter especial)";
        if (!new PhoneNumberValidator().validate(formData.get("phone")))
            return "Numero de telefono invalido";
        return null;
    }

    private User build(Map<String, String> formData) {
        String identifier = "user_" + System.currentTimeMillis();
        UserIdentity identity = new UserIdentity(identifier, formData.get("full_name"));
        String passwordHash = new PasswordHasher().hash(Objects.requireNonNull(formData.get("password")));
        Credentials credentials = new Credentials(formData.get("email"), passwordHash);
        UserContact contact = new UserContact(formData.get("username"), formData.get("phone"));
        String occupationIdentifier = listOccupationsUseCase.findIdentifier(formData.get("occupation"));
        UserProfile profile = new UserProfile(contact, occupationIdentifier);
        UserData data = new UserData(credentials, profile);
        return User.create(identity, data);
    }
}