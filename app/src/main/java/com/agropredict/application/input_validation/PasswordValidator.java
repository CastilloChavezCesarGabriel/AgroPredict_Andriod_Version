package com.agropredict.application.input_validation;

import com.agropredict.application.factory.failure.IPasswordFailureFactory;
import com.agropredict.domain.input_validation.ITextValidator;
import com.agropredict.domain.input_validation.gate.IValidationGate;
import com.agropredict.domain.input_validation.rule.MinimumLengthRule;
import com.agropredict.domain.input_validation.rule.PassphraseRequirementRule;
import com.agropredict.domain.input_validation.rule.PasswordPolicyRule;
import com.agropredict.domain.input_validation.rule.ValidatorChain;
import java.util.Objects;

public final class PasswordValidator extends ValidatorChain {
    private static final int MINIMUM_LENGTH = 12;
    private static final int PASSPHRASE_LENGTH = 16;
    private final IPasswordFailureFactory failureFactory;

    public PasswordValidator(IPasswordFailureFactory failureFactory) {
        this.failureFactory = Objects.requireNonNull(failureFactory, "password validator requires a failure factory");
    }

    @Override
    protected void inspect(String text, IValidationGate gate) {
        new MinimumLengthRule(MINIMUM_LENGTH, failureFactory.createUndersizedPassword()).check(text, gate);
        select(text).check(text, gate);
    }

    private ITextValidator select(String text) {
        if (text != null && text.length() >= PASSPHRASE_LENGTH) {
            return new PassphraseRequirementRule(failureFactory.createRepetitivePassphrase());
        }
        return new PasswordPolicyRule(failureFactory.createWeakPassword());
    }
}
