package com.agropredict.domain.input_validation;

public final class PhoneNumberValidator extends ValidatorChain {
    private static final int MINIMUM_LENGTH = 7;
    private static final int MAXIMUM_LENGTH = 15;
    private static final String PHONE_PATTERN = "^[0-9]+$";

    @Override
    protected void inspect(String text, IValidationGate gate) {
        if (text == null || text.isEmpty()) return;
        new LengthRangeRule(new Range(MINIMUM_LENGTH, MAXIMUM_LENGTH),
                "Phone number must be between " + MINIMUM_LENGTH + " and " + MAXIMUM_LENGTH + " digits")
            .check(text, gate);
        new RegexRule(PHONE_PATTERN, "Phone number may only contain digits")
            .check(text, gate);
    }
}
