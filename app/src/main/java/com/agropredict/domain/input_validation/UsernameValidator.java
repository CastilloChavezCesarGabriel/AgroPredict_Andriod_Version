package com.agropredict.domain.input_validation;

public final class UsernameValidator extends ValidatorChain {
    private static final int MINIMUM_LENGTH = 5;
    private static final int MAXIMUM_LENGTH = 32;
    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9_]+$";

    @Override
    protected void inspect(String text, IValidationGate gate) {
        new PresenceRule("Username is required").check(text, gate);
        new LengthRangeRule(new Range(MINIMUM_LENGTH, MAXIMUM_LENGTH),
                "Username must be between " + MINIMUM_LENGTH + " and " + MAXIMUM_LENGTH + " characters")
            .check(text, gate);
        new RegexRule(USERNAME_PATTERN,
                "Username may only contain letters, digits and underscores")
            .check(text, gate);
        new LetterRule("Username must contain at least one letter").check(text, gate);
    }
}
