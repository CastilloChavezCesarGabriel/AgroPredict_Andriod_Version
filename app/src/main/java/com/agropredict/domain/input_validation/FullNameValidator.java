package com.agropredict.domain.input_validation;

public final class FullNameValidator extends ValidatorChain {
    private static final int MINIMUM_LENGTH = 2;
    private static final int MAXIMUM_LENGTH = 50;
    private static final String NAME_PATTERN = "^(?=.*\\p{L})[\\p{L}\\s]+$";

    @Override
    protected void inspect(String text, IValidationGate gate) {
        String trimmed = text == null ? "" : text.trim();
        new PresenceRule("Full name is required").check(trimmed, gate);
        new LengthRangeRule(new Range(MINIMUM_LENGTH, MAXIMUM_LENGTH),
                "Full name must be between " + MINIMUM_LENGTH + " and " + MAXIMUM_LENGTH + " characters")
            .check(trimmed, gate);
        new RegexRule(NAME_PATTERN, "Full name may only contain letters and spaces")
            .check(trimmed, gate);
    }
}
