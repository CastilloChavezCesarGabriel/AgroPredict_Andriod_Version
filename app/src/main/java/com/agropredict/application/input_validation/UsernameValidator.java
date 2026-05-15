package com.agropredict.application.input_validation;

import com.agropredict.application.factory.failure.IUsernameFailureFactory;
import com.agropredict.domain.input_validation.gate.IValidationGate;
import com.agropredict.domain.input_validation.rule.LengthRangeRule;
import com.agropredict.domain.input_validation.rule.LetterRule;
import com.agropredict.domain.input_validation.rule.PresenceRule;
import com.agropredict.domain.input_validation.rule.Range;
import com.agropredict.domain.input_validation.rule.RegexRule;
import com.agropredict.domain.input_validation.rule.ValidatorChain;
import java.util.Objects;

public final class UsernameValidator extends ValidatorChain {
    private static final int MINIMUM_LENGTH = 5;
    private static final int MAXIMUM_LENGTH = 32;
    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9_]+$";
    private final IUsernameFailureFactory failureFactory;

    public UsernameValidator(IUsernameFailureFactory failureFactory) {
        this.failureFactory = Objects.requireNonNull(failureFactory, "username validator requires a failure factory");
    }

    @Override
    protected void inspect(String text, IValidationGate gate) {
        new PresenceRule(failureFactory.createEmptyUsername()).check(text, gate);
        new LengthRangeRule(new Range(MINIMUM_LENGTH, MAXIMUM_LENGTH),
                failureFactory.createInvalidUsernameLength()).check(text, gate);
        new RegexRule(USERNAME_PATTERN, failureFactory.createInvalidUsernameCharacter()).check(text, gate);
        new LetterRule(failureFactory.createLetterlessUsername()).check(text, gate);
    }
}
