package com.agropredict.presentation.user_interface.questionnaire_input;

import android.app.Activity;
import android.widget.EditText;
import com.agropredict.R;
import com.agropredict.application.request.user_registration.Credential;

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
        return password.equals(confirmation);
    }

    public Credential collect() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString();
        return new Credential(email, password);
    }
}