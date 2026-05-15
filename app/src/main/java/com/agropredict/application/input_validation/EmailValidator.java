package com.agropredict.application.input_validation;

import com.agropredict.application.factory.failure.IEmailFailureFactory;
import com.agropredict.domain.input_validation.gate.IValidationGate;
import com.agropredict.domain.input_validation.rule.PresenceRule;
import com.agropredict.domain.input_validation.rule.RegexRule;
import com.agropredict.domain.input_validation.rule.ValidatorChain;
import java.util.Objects;

public final class EmailValidator extends ValidatorChain {
    private static final String EMAIL_PATTERN =
            "^[a-zA-Z0-9]([a-zA-Z0-9_-]*(\\.[a-zA-Z0-9_-]+)*)@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private final IEmailFailureFactory failureFactory;

    public EmailValidator(IEmailFailureFactory failureFactory) {
        this.failureFactory = Objects.requireNonNull(failureFactory,
                "email validator requires a failure factory");
    }

    @Override
    protected void inspect(String text, IValidationGate gate) {
        new PresenceRule(failureFactory.createInvalidEmail()).check(text, gate);
        new RegexRule(EMAIL_PATTERN, failureFactory.createInvalidEmail()).check(text, gate);
    }
}