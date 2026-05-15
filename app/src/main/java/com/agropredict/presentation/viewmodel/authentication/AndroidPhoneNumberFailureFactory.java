package com.agropredict.presentation.viewmodel.authentication;

import android.content.Context;
import com.agropredict.R;
import com.agropredict.application.factory.failure.IPhoneNumberFailureFactory;
import com.agropredict.domain.input_validation.failure.IValidationFailure;
import com.agropredict.domain.input_validation.failure.ValidationFailure;
import java.util.Objects;

public final class AndroidPhoneNumberFailureFactory implements IPhoneNumberFailureFactory {
    private final Context context;

    public AndroidPhoneNumberFailureFactory(Context context) {
        this.context = Objects.requireNonNull(context, "android phone number failure factory requires a context");
    }

    @Override
    public IValidationFailure createInvalidPhoneNumberLength() {
        return new ValidationFailure(() -> context.getString(R.string.validation_phone_number_invalid_length));
    }

    @Override
    public IValidationFailure createInvalidPhoneNumberCharacter() {
        return new ValidationFailure(() -> context.getString(R.string.validation_phone_number_invalid_character));
    }
}
