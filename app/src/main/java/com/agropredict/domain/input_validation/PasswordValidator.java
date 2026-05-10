package com.agropredict.domain.input_validation;

public final class PasswordValidator extends ValidatorChain {
    private static final int MINIMUM_LENGTH = 12;
    private static final int PASSPHRASE_LENGTH = 16;

    @Override
    protected void inspect(String text, IValidationGate gate) {
        new MinimumLengthRule(MINIMUM_LENGTH,
                "Password must be at least " + MINIMUM_LENGTH + " characters")
            .check(text, gate);
        select(text).check(text, gate);
    }

    private ITextValidator select(String text) {
        if (text.length() >= PASSPHRASE_LENGTH) {
            return new PassphraseRequirementRule("Passphrase must contain enough distinct characters");
        }
        return new PasswordPolicyRule("Passwords under " + PASSPHRASE_LENGTH
                + " characters must include upper, lower, digit and special characters");
    }
}