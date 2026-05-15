package com.agropredict.presentation.viewmodel.authentication;

import android.content.Context;
import com.agropredict.R;
import com.agropredict.application.factory.failure.IFullNameFailureFactory;
import com.agropredict.domain.input_validation.failure.IValidationFailure;
import com.agropredict.domain.input_validation.failure.ValidationFailure;
import java.util.Objects;

public final class AndroidFullNameFailureFactory implements IFullNameFailureFactory {
    private final Context context;

    public AndroidFullNameFailureFactory(Context context) {
        this.context = Objects.requireNonNull(context, "android full name failure factory requires a context");
    }

    @Override
    public IValidationFailure createEmptyFullName() {
        return new ValidationFailure(() -> context.getString(R.string.validation_full_name_empty));
    }

    @Override
    public IValidationFailure createInvalidFullNameLength() {
        return new ValidationFailure(() -> context.getString(R.string.validation_full_name_invalid_length));
    }

    @Override
    public IValidationFailure createInvalidFullNameCharacter() {
        return new ValidationFailure(() -> context.getString(R.string.validation_full_name_invalid_character));
    }
}
