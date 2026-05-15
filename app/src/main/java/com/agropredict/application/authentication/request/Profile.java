package com.agropredict.application.authentication.request;

import com.agropredict.application.factory.failure.IUsernameFailureFactory;
import com.agropredict.application.operation_result.IUseCaseResult;
import com.agropredict.application.operation_result.SuccessfulOperation;
import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.input_validation.UsernameValidator;
import com.agropredict.domain.guard.ArgumentPrecondition;
import com.agropredict.domain.input_validation.gate.ValidationGate;
import com.agropredict.domain.user.visitor.IOccupationConsumer;
import com.agropredict.domain.user.visitor.IUsernameConsumer;
import java.util.Objects;

public final class Profile {
    private final String username;
    private final String occupation;
    private final IUsernameFailureFactory failureFactory;

    public Profile(String username, String occupation, IUsernameFailureFactory failureFactory) {
        this.username = ArgumentPrecondition.validate(username, "profile username");
        this.occupation = ArgumentPrecondition.validate(occupation, "profile occupation");
        this.failureFactory = Objects.requireNonNull(failureFactory, "profile requires a failure factory");
    }

    public void validate() {
        new UsernameValidator(failureFactory).check(username, new ValidationGate());
    }

    public void enroll(IUsernameConsumer consumer) {
        consumer.enroll(username);
    }

    public void classify(IOccupationConsumer consumer, ICatalogRepository catalog) {
        consumer.classify(catalog.resolve(occupation));
    }

    public IUseCaseResult succeed() {
        return new SuccessfulOperation(username);
    }
}
