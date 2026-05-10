package com.agropredict.domain.input_validation;

import java.util.List;

public final class PasswordPolicyRule extends TextRule {
    private static final List<ICharacterRequirement> REQUIREMENTS = List.of(
            new UppercaseRequirement(),
            new LowercaseRequirement(),
            new DigitRequirement(),
            new SpecialCharacterRequirement());

    public PasswordPolicyRule(String reason) {
        super(reason);
    }

    @Override
    protected boolean accepts(String text) {
        return text != null && new PasswordPolicy(REQUIREMENTS).accepts(text);
    }
}