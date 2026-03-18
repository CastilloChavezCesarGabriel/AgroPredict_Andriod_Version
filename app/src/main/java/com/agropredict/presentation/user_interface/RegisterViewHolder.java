package com.agropredict.presentation.user_interface;

import android.app.Activity;
import android.widget.EditText;
import android.widget.Spinner;
import com.agropredict.R;
import java.util.HashMap;
import java.util.Map;

public final class RegisterViewHolder {
    private final EditText fullNameInput;
    private final EditText emailInput;
    private final EditText phoneInput;
    private final EditText usernameInput;
    private final EditText passwordInput;
    private final EditText confirmPasswordInput;
    private final Spinner occupationSpinner;

    public RegisterViewHolder(Activity activity) {
        this.fullNameInput = activity.findViewById(R.id.etFullName);
        this.emailInput = activity.findViewById(R.id.etEmail);
        this.phoneInput = activity.findViewById(R.id.etPhone);
        this.usernameInput = activity.findViewById(R.id.etUsername);
        this.passwordInput = activity.findViewById(R.id.etPassword);
        this.confirmPasswordInput = activity.findViewById(R.id.etConfirmPassword);
        this.occupationSpinner = activity.findViewById(R.id.spnOccupation);
    }

    public boolean matchPasswords() {
        String password = passwordInput.getText().toString();
        String confirmation = confirmPasswordInput.getText().toString();
        return password.equals(confirmation);
    }

    public Map<String, String> collect() {
        Map<String, String> formData = new HashMap<>();
        formData.put("full_name", fullNameInput.getText().toString().trim());
        formData.put("email", emailInput.getText().toString().trim());
        formData.put("phone", phoneInput.getText().toString().trim());
        formData.put("username", usernameInput.getText().toString().trim());
        formData.put("password", passwordInput.getText().toString());
        formData.put("occupation", occupationSpinner.getSelectedItem().toString());
        return formData;
    }

    public void populateOccupations(java.util.List<String> occupations) {
        SpinnerPopulator.populate(occupationSpinner, occupations);
    }
}
