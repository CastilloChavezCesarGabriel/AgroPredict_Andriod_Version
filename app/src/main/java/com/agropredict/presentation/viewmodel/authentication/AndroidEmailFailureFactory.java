package com.agropredict.presentation.viewmodel.authentication;

import android.content.Context;
import com.agropredict.R;
import com.agropredict.application.factory.failure.IEmailFailureFactory;
import com.agropredict.domain.input_validation.failure.IValidationFailure;
import com.agropredict.domain.input_validation.failure.ValidationFailure;
import java.util.Objects;

public final class AndroidEmailFailureFactory implements IEmailFailureFactory {
    private final Context context;

    public AndroidEmailFailureFactory(Context context) {
        this.context = Objects.requireNonNull(context, "android email failure factory requires a context");
    }

    @Override
    public IValidationFailure createInvalidEmail() {
        return new ValidationFailure(() -> context.getString(R.string.validation_email_invalid));
    }
}
