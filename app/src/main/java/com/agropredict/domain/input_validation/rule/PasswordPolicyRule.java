package com.agropredict.domain.input_validation.rule;

import com.agropredict.domain.input_validation.requirement.DigitRequirement;
import com.agropredict.domain.input_validation.requirement.LowercaseRequirement;
import com.agropredict.domain.input_validation.requirement.PasswordPolicy;
import com.agropredict.domain.input_validation.requirement.SpecialCharacterRequirement;
import com.agropredict.domain.input_validation.requirement.UppercaseRequirement;
import com.agropredict.domain.input_validation.failure.IValidationFailure;
import java.util.List;

public final class PasswordPolicyRule extends TextRule {
    private final PasswordPolicy policy;

    public PasswordPolicyRule(IValidationFailure failure) {
        super(failure);
        this.policy = new PasswordPolicy(List.of(
                new UppercaseRequirement(),
                new LowercaseRequirement(),
                new DigitRequirement(),
                new SpecialCharacterRequirement()));
    }

    @Override
    protected boolean accepts(String text) {
        return text != null && policy.accepts(text);
    }
}
