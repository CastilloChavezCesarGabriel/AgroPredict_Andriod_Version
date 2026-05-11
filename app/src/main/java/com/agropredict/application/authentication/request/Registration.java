package com.agropredict.application.authentication.request;

import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.service.IPasswordHasher;
import com.agropredict.domain.user.visitor.ICredentialConsumer;
import com.agropredict.domain.user.visitor.IOccupationConsumer;
import com.agropredict.domain.user.visitor.IPhoneConsumer;
import com.agropredict.domain.user.visitor.IUserIdentityConsumer;
import com.agropredict.domain.user.visitor.IUsernameConsumer;
import java.util.Objects;

public final class Registration {
    private final Registrant personal;
    private final Credential credential;
    private final Profile profile;

    public Registration(Registrant personal, Credential credential, Profile profile) {
        this.personal = Objects.requireNonNull(personal, "registration requires a registrant");
        this.credential = Objects.requireNonNull(credential, "registration requires a credential");
        this.profile = Objects.requireNonNull(profile, "registration requires a profile");
    }

    public void validate() {
        personal.validate();
        credential.validate();
        profile.validate();
    }

    public void describe(IUserIdentityConsumer consumer, String identifier) {
        personal.describe(consumer, identifier);
    }

    public void contact(IPhoneConsumer consumer) {
        personal.contact(consumer);
    }

    public void authenticate(ICredentialConsumer consumer, IPasswordHasher hasher) {
        credential.submit(consumer, hasher);
    }

    public void enroll(IUsernameConsumer consumer) {
        profile.enroll(consumer);
    }

    public void classify(IOccupationConsumer consumer, ICatalogRepository catalog) {
        profile.classify(consumer, catalog);
    }
}
