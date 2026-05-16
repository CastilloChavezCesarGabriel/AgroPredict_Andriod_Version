package com.agropredict.presentation.user_interface.questionnaire_input;

import android.app.Activity;
import com.agropredict.application.authentication.request.CredentialDraft;
import com.agropredict.application.authentication.request.CredentialFailureContext;
import com.agropredict.application.authentication.request.Profile;
import com.agropredict.application.authentication.request.Registrant;
import com.agropredict.application.authentication.request.RegistrantFailureContext;
import com.agropredict.application.factory.failure.IUsernameFailureFactory;
import com.agropredict.presentation.viewmodel.authentication.AndroidEmailFailureFactory;
import com.agropredict.presentation.viewmodel.authentication.AndroidFullNameFailureFactory;
import com.agropredict.presentation.viewmodel.authentication.AndroidPasswordFailureFactory;
import com.agropredict.presentation.viewmodel.authentication.AndroidPhoneNumberFailureFactory;
import com.agropredict.presentation.viewmodel.authentication.AndroidUsernameFailureFactory;
import java.util.Objects;

public final class EnrollmentGuard {
    private final CredentialFailureContext credentialFailures;
    private final RegistrantFailureContext registrantFailures;
    private final IUsernameFailureFactory usernameFailures;

    public EnrollmentGuard(Activity activity) {
        Objects.requireNonNull(activity, "enrollment guard requires an activity");
        this.credentialFailures = new CredentialFailureContext(
                new AndroidEmailFailureFactory(activity),
                new AndroidPasswordFailureFactory(activity));
        this.registrantFailures = new RegistrantFailureContext(
                new AndroidFullNameFailureFactory(activity),
                new AndroidPhoneNumberFailureFactory(activity));
        this.usernameFailures = new AndroidUsernameFailureFactory(activity);
    }

    public Registrant createRegistrant(String fullName, String phone) {
        return new Registrant(fullName, phone, registrantFailures);
    }

    public Profile createProfile(String username, String occupation) {
        return new Profile(username, occupation, usernameFailures);
    }

    public CredentialDraft createCredential(CredentialInput input) {
        return input.collect(credentialFailures);
    }
}
