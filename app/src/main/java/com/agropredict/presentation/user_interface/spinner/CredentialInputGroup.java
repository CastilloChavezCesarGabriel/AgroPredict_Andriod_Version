package com.agropredict.presentation.user_interface.spinner;

import android.app.Activity;
import android.widget.EditText;
import com.agropredict.R;
import com.agropredict.application.request.data.Authentication;

public final class CredentialInputGroup {
    private final EditText emailInput;
    private final EditText passwordInput;
    private final EditText confirmPasswordInput;

    public CredentialInputGroup(Activity activity) {
        this.emailInput = activity.findViewById(R.id.etEmail);
        this.passwordInput = activity.findViewById(R.id.etPassword);
        this.confirmPasswordInput = activity.findViewById(R.id.etConfirmPassword);
    }

    public boolean match() {
        String password = passwordInput.getText().toString();
        String confirmation = confirmPasswordInput.getText().toString();
        return password.equals(confirmation);
    }

    public Authentication collect() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString();
        return new Authentication(email, password);
    }
}
