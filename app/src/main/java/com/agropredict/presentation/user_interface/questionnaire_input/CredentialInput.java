package com.agropredict.presentation.user_interface.questionnaire_input;

import android.app.Activity;
import android.widget.EditText;
import com.agropredict.R;
import com.agropredict.application.request.user_registration.Authentication;
import com.agropredict.domain.input_validation.PasswordValidator;

public final class CredentialInput {
    private final EditText emailInput;
    private final EditText passwordInput;
    private final EditText confirmPasswordInput;

    public CredentialInput(Activity activity) {
        this.emailInput = activity.findViewById(R.id.etEmail);
        this.passwordInput = activity.findViewById(R.id.etPassword);
        this.confirmPasswordInput = activity.findViewById(R.id.etConfirmPassword);
    }

    public boolean isMatching() {
        String password = passwordInput.getText().toString();
        String confirmation = confirmPasswordInput.getText().toString();
        return new PasswordValidator().isConfirmed(password, confirmation);
    }

    public Authentication collect() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString();
        return new Authentication(email, password);
    }
}