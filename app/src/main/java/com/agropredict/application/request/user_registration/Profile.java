package com.agropredict.application.request.user_registration;

import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.domain.input_validation.UsernameValidator;
import com.agropredict.domain.visitor.user.IUserVisitor;

public final class Profile {
    private final String username;
    private final String occupation;

    public Profile(String username, String occupation) {
        this.username = username;
        this.occupation = occupation;
    }

    public void validate() {
        if (!new UsernameValidator().isValid(username))
            throw new RegistrationException("Invalid username");
    }

    public void dispatch(IUserVisitor visitor) {
        visitor.visitUsername(username);
    }

    public void classify(IUserVisitor visitor, ICatalogRepository catalog) {
        visitor.visitOccupation(catalog.resolve(occupation));
    }
}