package com.agropredict.presentation.viewmodel.authentication;

import android.content.Context;
import com.agropredict.R;
import com.agropredict.application.factory.failure.IUsernameFailureFactory;
import com.agropredict.domain.input_validation.failure.IValidationFailure;
import com.agropredict.domain.input_validation.failure.ValidationFailure;
import java.util.Objects;

public final class AndroidUsernameFailureFactory implements IUsernameFailureFactory {
    private final Context context;

    public AndroidUsernameFailureFactory(Context context) {
        this.context = Objects.requireNonNull(context, "android username failure factory requires a context");
    }

    @Override
    public IValidationFailure createEmptyUsername() {
        return new ValidationFailure(() -> context.getString(R.string.validation_username_empty));
    }

    @Override
    public IValidationFailure createInvalidUsernameLength() {
        return new ValidationFailure(() -> context.getString(R.string.validation_username_invalid_length));
    }

    @Override
    public IValidationFailure createInvalidUsernameCharacter() {
        return new ValidationFailure(() -> context.getString(R.string.validation_username_invalid_character));
    }

    @Override
    public IValidationFailure createLetterlessUsername() {
        return new ValidationFailure(() -> context.getString(R.string.validation_username_letterless));
    }
}
