package com.agropredict.application.request.data;

import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.request.RegistrationException;
import com.agropredict.domain.component.user.UserProfile;
import com.agropredict.domain.component.user.UserContact;
import com.agropredict.domain.validation.UsernameValidator;

public final class Profile {
    private final String username;
    private final String occupation;

    public Profile(String username, String occupation) {
        this.username = username;
        this.occupation = occupation;
    }

    public void validate(IUserRepository repository) {
        if (!new UsernameValidator().validate(username))
            throw new RegistrationException("Nombre de usuario invalido");
        if (repository.isTaken(username))
            throw new RegistrationException("El nombre de usuario ya existe");
    }

    public UserProfile establish(Registrant personal, ICatalogRepository catalog) {
        String occupationId = catalog.resolve(occupation);
        UserContact contact = personal.enroll(username);
        return new UserProfile(contact, occupationId);
    }
}