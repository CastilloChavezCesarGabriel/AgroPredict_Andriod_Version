package com.agropredict.presentation.user_interface.questionnaire_input;

import android.app.Activity;
import android.widget.EditText;
import com.agropredict.R;
import java.util.Objects;

public final class LoginCredentialInput {
    private final EditText emailInput;
    private final EditText passwordInput;

    public LoginCredentialInput(Activity activity) {
        Objects.requireNonNull(activity, "login credential input requires an activity");
        this.emailInput = activity.findViewById(R.id.etEmail);
        this.passwordInput = activity.findViewById(R.id.etPassword);
    }

    public void submit(ILoginCredentialConsumer consumer, Runnable onIncomplete) {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString();
        if (email.isEmpty() || password.isEmpty()) {
            onIncomplete.run();
            return;
        }
        consumer.accept(email, password);
    }
}
