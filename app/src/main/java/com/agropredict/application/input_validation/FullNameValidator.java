package com.agropredict.application.input_validation;

import com.agropredict.application.factory.failure.IFullNameFailureFactory;
import com.agropredict.domain.input_validation.gate.IValidationGate;
import com.agropredict.domain.input_validation.rule.LengthRangeRule;
import com.agropredict.domain.input_validation.rule.PresenceRule;
import com.agropredict.domain.input_validation.rule.Range;
import com.agropredict.domain.input_validation.rule.RegexRule;
import com.agropredict.domain.input_validation.rule.ValidatorChain;
import java.util.Objects;

public final class FullNameValidator extends ValidatorChain {
    private static final int MINIMUM_LENGTH = 2;
    private static final int MAXIMUM_LENGTH = 50;
    private static final String NAME_PATTERN = "^(?=.*\\p{L})[\\p{L}\\s]+$";
    private final IFullNameFailureFactory failureFactory;

    public FullNameValidator(IFullNameFailureFactory failureFactory) {
        this.failureFactory = Objects.requireNonNull(failureFactory,
                "full name validator requires a failure factory");
    }

    @Override
    protected void inspect(String text, IValidationGate gate) {
        String trimmed = text == null ? "" : text.trim();
        new PresenceRule(failureFactory.createEmptyFullName()).check(trimmed, gate);
        new LengthRangeRule(new Range(MINIMUM_LENGTH, MAXIMUM_LENGTH),
                failureFactory.createInvalidFullNameLength()).check(trimmed, gate);
        new RegexRule(NAME_PATTERN, failureFactory.createInvalidFullNameCharacter()).check(trimmed, gate);
    }
}
