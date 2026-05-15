package com.agropredict.presentation.user_interface.questionnaire_input;

import android.app.Activity;
import android.widget.EditText;
import com.agropredict.R;
import com.agropredict.application.authentication.request.CredentialDraft;
import com.agropredict.application.authentication.request.CredentialFailureContext;

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

    public CredentialDraft collect(CredentialFailureContext failureContext) {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString();
        return new CredentialDraft(email, password, failureContext);
    }
}
