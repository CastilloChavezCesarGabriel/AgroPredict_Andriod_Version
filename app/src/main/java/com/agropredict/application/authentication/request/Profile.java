package com.agropredict.application.authentication.request;

import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.domain.input_validation.UsernameValidator;
import com.agropredict.domain.input_validation.ValidationGate;
import com.agropredict.domain.user.visitor.IOccupationConsumer;
import com.agropredict.domain.user.visitor.IUsernameConsumer;
import java.util.Objects;

public final class Profile {
    private final String username;
    private final String occupation;

    public Profile(String username, String occupation) {
        this.username = Objects.requireNonNull(username, "profile requires a username");
        this.occupation = Objects.requireNonNull(occupation, "profile requires an occupation");
    }

    public void validate() {
        new UsernameValidator().check(username, new ValidationGate());
    }

    public void enroll(IUsernameConsumer consumer) {
        consumer.enroll(username);
    }

    public void classify(IOccupationConsumer consumer, ICatalogRepository catalog) {
        consumer.classify(catalog.resolve(occupation));
    }
}