package com.agropredict.presentation.viewmodel.authentication;

import android.content.Context;
import com.agropredict.R;
import com.agropredict.application.factory.failure.IPasswordFailureFactory;
import com.agropredict.domain.input_validation.failure.IValidationFailure;
import com.agropredict.domain.input_validation.failure.ValidationFailure;
import java.util.Objects;

public final class AndroidPasswordFailureFactory implements IPasswordFailureFactory {
    private final Context context;

    public AndroidPasswordFailureFactory(Context context) {
        this.context = Objects.requireNonNull(context, "android password failure factory requires a context");
    }

    @Override
    public IValidationFailure createUndersizedPassword() {
        return new ValidationFailure(() -> context.getString(R.string.validation_password_undersized));
    }

    @Override
    public IValidationFailure createRepetitivePassphrase() {
        return new ValidationFailure(() -> context.getString(R.string.validation_passphrase_repetitive));
    }

    @Override
    public IValidationFailure createWeakPassword() {
        return new ValidationFailure(() -> context.getString(R.string.validation_password_weak));
    }
}
