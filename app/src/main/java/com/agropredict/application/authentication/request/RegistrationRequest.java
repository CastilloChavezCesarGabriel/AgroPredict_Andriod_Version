package com.agropredict.application.authentication.request;

import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.service.IPasswordHasher;
import com.agropredict.domain.guard.ArgumentPrecondition;
import com.agropredict.domain.identifier.IIdentifierConsumer;
import com.agropredict.domain.identifier.IdentifierFactory;
import com.agropredict.domain.user.visitor.ICredentialConsumer;
import com.agropredict.domain.user.visitor.IOccupationConsumer;
import com.agropredict.domain.user.visitor.IPhoneConsumer;
import com.agropredict.domain.user.visitor.IUserIdentityConsumer;
import com.agropredict.domain.user.visitor.IUsernameConsumer;
import java.util.Objects;

public final class RegistrationRequest {
    private final String identifier;
    private final Registration registration;

    public RegistrationRequest(String identifier, Registration registration) {
        this.identifier = ArgumentPrecondition.validate(identifier, "registration request identifier");
        this.registration = Objects.requireNonNull(registration, "registration request requires a registration");
    }

    public static RegistrationRequest compose(Registration registration) {
        return new RegistrationRequest(IdentifierFactory.generate("user"), registration);
    }

    public void validate() {
        registration.validate();
    }

    public void describe(IUserIdentityConsumer consumer) {
        registration.describe(consumer, identifier);
    }

    public void contact(IPhoneConsumer consumer) {
        registration.contact(consumer);
    }

    public void authenticate(ICredentialConsumer consumer, IPasswordHasher hasher) {
        registration.authenticate(consumer, hasher);
    }

    public void enroll(IUsernameConsumer consumer) {
        registration.enroll(consumer);
    }

    public void classify(IOccupationConsumer consumer, ICatalogRepository catalog) {
        registration.classify(consumer, catalog);
    }

    public void identify(IIdentifierConsumer consumer) {
        consumer.identify(identifier);
    }
}
