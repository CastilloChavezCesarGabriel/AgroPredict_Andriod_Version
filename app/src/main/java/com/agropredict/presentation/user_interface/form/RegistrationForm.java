package com.agropredict.presentation.user_interface.form;

import com.agropredict.presentation.user_interface.questionnaire_input.CredentialInput;
import com.agropredict.presentation.user_interface.catalog_input.SpinnerPopulator;
import android.app.Activity;
import android.widget.EditText;
import android.widget.Spinner;
import com.agropredict.R;
import com.agropredict.application.request.user_registration.Registrant;
import com.agropredict.application.request.user_registration.Profile;
import com.agropredict.application.request.user_registration.Registration;
import com.agropredict.application.request.user_registration.RegistrationRequest;

public final class RegistrationForm {
    private final EditText fullNameInput;
    private final EditText phoneInput;
    private final EditText usernameInput;
    private final Spinner occupationSpinner;
    private final CredentialInput credentialGroup;

    public RegistrationForm(Activity activity) {
        this.fullNameInput = activity.findViewById(R.id.etFullName);
        this.phoneInput = activity.findViewById(R.id.etPhone);
        this.usernameInput = activity.findViewById(R.id.etUsername);
        this.occupationSpinner = activity.findViewById(R.id.spnOccupation);
        this.credentialGroup = new CredentialInput(activity);
    }

    public boolean isMatching() {
        return credentialGroup.isMatching();
    }

    public RegistrationRequest collect() {
        String fullName = fullNameInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        Registrant personalInformation = new Registrant(fullName, phone);
        String username = usernameInput.getText().toString().trim();
        String occupation = occupationSpinner.getSelectedItem().toString();
        Profile profile = new Profile(username, occupation);
        return RegistrationRequest.compose(new Registration(personalInformation, credentialGroup.collect(), profile));
    }

    public void populate(java.util.List<String> occupations) {
        SpinnerPopulator.populate(occupationSpinner, occupations);
    }
}