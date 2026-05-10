package com.agropredict.domain.input_validation;

public final class EmailValidator extends ValidatorChain {
    private static final String EMAIL_PATTERN =
            "^[a-zA-Z0-9]([a-zA-Z0-9_-]*(\\.[a-zA-Z0-9_-]+)*)@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    @Override
    protected void inspect(String text, IValidationGate gate) {
        new PresenceRule("Invalid email format").check(text, gate);
        new RegexRule(EMAIL_PATTERN, "Invalid email format").check(text, gate);
    }
}