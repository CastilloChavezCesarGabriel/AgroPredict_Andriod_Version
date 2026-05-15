package com.agropredict.domain.input_validation.failure;

import com.agropredict.domain.input_validation.phrase.IValidationFailureCallback;
import com.agropredict.domain.input_validation.phrase.IValidationFailurePhrase;
import java.util.Objects;

public final class ValidationFailure implements IValidationFailure {
    private final IValidationFailurePhrase phrase;

    public ValidationFailure(IValidationFailurePhrase phrase) {
        this.phrase = Objects.requireNonNull(phrase, "validation failure requires a phrase");
    }

    @Override
    public void encode(IValidationFailureCallback callback) {
        callback.receive(phrase.describe());
    }
}
