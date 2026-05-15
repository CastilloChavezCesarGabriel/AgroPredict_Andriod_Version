package com.agropredict.presentation.viewmodel.authentication;

import android.content.Context;
import com.agropredict.R;
import com.agropredict.application.factory.failure.IAuthenticationFailureFactory;
import com.agropredict.domain.authentication.failure.AuthenticationFailure;
import com.agropredict.domain.authentication.failure.IAuthenticationFailure;
import java.util.Objects;

public final class AndroidAuthenticationFailureFactory implements IAuthenticationFailureFactory {
    private final Context context;

    public AndroidAuthenticationFailureFactory(Context context) {
        this.context = Objects.requireNonNull(context, "android authentication failure factory requires a context");
    }

    @Override
    public IAuthenticationFailure createIncorrectCredential() {
        return new AuthenticationFailure(() -> context.getString(R.string.authentication_incorrect_credential));
    }

    @Override
    public IAuthenticationFailure createLockedAccount() {
        return new AuthenticationFailure(() -> context.getString(R.string.authentication_locked_account));
    }

    @Override
    public IAuthenticationFailure createExhaustedAttempt() {
        return new AuthenticationFailure(() -> context.getString(R.string.authentication_exhausted_attempt));
    }
}
