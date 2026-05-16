package com.agropredict.presentation.user_interface.form;

import android.app.Activity;
import com.agropredict.application.authentication.request.Registration;
import com.agropredict.application.authentication.request.RegistrationRequest;
import com.agropredict.presentation.user_interface.questionnaire_input.CredentialInput;
import com.agropredict.presentation.user_interface.questionnaire_input.EnrollmentGuard;
import com.agropredict.presentation.user_interface.questionnaire_input.RegistrantInput;
import java.util.List;

public final class RegistrationForm {
    private final RegistrantInput registrantInput;
    private final CredentialInput credentialGroup;
    private final EnrollmentGuard guard;

    public RegistrationForm(Activity activity) {
        this.registrantInput = new RegistrantInput(activity);
        this.credentialGroup = new CredentialInput(activity);
        this.guard = new EnrollmentGuard(activity);
    }

    public boolean isMatching() {
        return credentialGroup.isMatching();
    }

    public RegistrationRequest collect() {
        return RegistrationRequest.compose(new Registration(
                registrantInput.identify(guard),
                guard.createCredential(credentialGroup),
                registrantInput.classify(guard)));
    }

    public void populate(List<String> occupations) {
        registrantInput.populate(occupations);
    }
}
