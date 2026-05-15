package com.agropredict.presentation.user_interface.form;

import com.agropredict.presentation.user_interface.questionnaire_input.CredentialInput;
import com.agropredict.presentation.user_interface.catalog_input.SpinnerPopulator;
import android.app.Activity;
import android.widget.EditText;
import android.widget.Spinner;
import com.agropredict.R;
import com.agropredict.application.authentication.request.CredentialFailureContext;
import com.agropredict.application.authentication.request.Profile;
import com.agropredict.application.authentication.request.Registrant;
import com.agropredict.application.authentication.request.RegistrantFailureContext;
import com.agropredict.application.authentication.request.Registration;
import com.agropredict.application.authentication.request.RegistrationRequest;
import com.agropredict.application.factory.failure.IUsernameFailureFactory;
import com.agropredict.presentation.viewmodel.authentication.AndroidEmailFailureFactory;
import com.agropredict.presentation.viewmodel.authentication.AndroidFullNameFailureFactory;
import com.agropredict.presentation.viewmodel.authentication.AndroidPasswordFailureFactory;
import com.agropredict.presentation.viewmodel.authentication.AndroidPhoneNumberFailureFactory;
import com.agropredict.presentation.viewmodel.authentication.AndroidUsernameFailureFactory;

public final class RegistrationForm {
    private final EditText fullNameInput;
    private final EditText phoneInput;
    private final EditText usernameInput;
    private final Spinner occupationSpinner;
    private final CredentialInput credentialGroup;
    private final CredentialFailureContext credentialFailureContext;
    private final RegistrantFailureContext registrantFailureContext;
    private final IUsernameFailureFactory usernameFailureFactory;

    public RegistrationForm(Activity activity) {
        this.fullNameInput = activity.findViewById(R.id.etFullName);
        this.phoneInput = activity.findViewById(R.id.etPhone);
        this.usernameInput = activity.findViewById(R.id.etUsername);
        this.occupationSpinner = activity.findViewById(R.id.spnOccupation);
        this.credentialGroup = new CredentialInput(activity);
        this.credentialFailureContext = new CredentialFailureContext(
                new AndroidEmailFailureFactory(activity),
                new AndroidPasswordFailureFactory(activity));
        this.registrantFailureContext = new RegistrantFailureContext(
                new AndroidFullNameFailureFactory(activity),
                new AndroidPhoneNumberFailureFactory(activity));
        this.usernameFailureFactory = new AndroidUsernameFailureFactory(activity);
    }

    public boolean isMatching() {
        return credentialGroup.isMatching();
    }

    public RegistrationRequest collect() {
        String fullName = fullNameInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        Registrant personalInformation = new Registrant(fullName, phone, registrantFailureContext);
        String username = usernameInput.getText().toString().trim();
        String occupation = occupationSpinner.getSelectedItem().toString();
        Profile profile = new Profile(username, occupation, usernameFailureFactory);
        return RegistrationRequest.compose(new Registration(personalInformation, credentialGroup.collect(credentialFailureContext), profile));
    }

    public void populate(java.util.List<String> occupations) {
        SpinnerPopulator.populate(occupationSpinner, occupations);
    }
}
