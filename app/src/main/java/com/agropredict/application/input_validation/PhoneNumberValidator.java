package com.agropredict.application.input_validation;

import com.agropredict.application.factory.failure.IPhoneNumberFailureFactory;
import com.agropredict.domain.input_validation.gate.IValidationGate;
import com.agropredict.domain.input_validation.rule.LengthRangeRule;
import com.agropredict.domain.input_validation.rule.Range;
import com.agropredict.domain.input_validation.rule.RegexRule;
import com.agropredict.domain.input_validation.rule.ValidatorChain;
import java.util.Objects;

public final class PhoneNumberValidator extends ValidatorChain {
    private static final int MINIMUM_LENGTH = 7;
    private static final int MAXIMUM_LENGTH = 15;
    private static final String PHONE_PATTERN = "^[0-9]+$";
    private final IPhoneNumberFailureFactory failureFactory;

    public PhoneNumberValidator(IPhoneNumberFailureFactory failureFactory) {
        this.failureFactory = Objects.requireNonNull(failureFactory, "phone number validator requires a failure factory");
    }

    @Override
    protected void inspect(String text, IValidationGate gate) {
        if (text == null || text.isEmpty()) return;
        new LengthRangeRule(new Range(MINIMUM_LENGTH, MAXIMUM_LENGTH),
                failureFactory.createInvalidPhoneNumberLength()).check(text, gate);
        new RegexRule(PHONE_PATTERN, failureFactory.createInvalidPhoneNumberCharacter()).check(text, gate);
    }
}
