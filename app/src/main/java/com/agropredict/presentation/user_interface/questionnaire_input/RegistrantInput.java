package com.agropredict.presentation.user_interface.questionnaire_input;

import android.app.Activity;
import android.widget.EditText;
import android.widget.Spinner;
import com.agropredict.R;
import com.agropredict.application.authentication.request.Profile;
import com.agropredict.application.authentication.request.Registrant;
import com.agropredict.presentation.user_interface.catalog_input.SpinnerPopulator;
import java.util.List;
import java.util.Objects;

public final class RegistrantInput {
    private final EditText fullNameInput;
    private final EditText phoneInput;
    private final EditText usernameInput;
    private final Spinner occupationSpinner;

    public RegistrantInput(Activity activity) {
        Objects.requireNonNull(activity, "registrant input requires an activity");
        this.fullNameInput = activity.findViewById(R.id.etFullName);
        this.phoneInput = activity.findViewById(R.id.etPhone);
        this.usernameInput = activity.findViewById(R.id.etUsername);
        this.occupationSpinner = activity.findViewById(R.id.spnOccupation);
    }

    public Registrant identify(EnrollmentGuard guard) {
        String fullName = fullNameInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        return guard.createRegistrant(fullName, phone);
    }

    public Profile classify(EnrollmentGuard guard) {
        String username = usernameInput.getText().toString().trim();
        String occupation = occupationSpinner.getSelectedItem().toString();
        return guard.createProfile(username, occupation);
    }

    public void populate(List<String> occupations) {
        new SpinnerPopulator().populate(occupationSpinner, occupations);
    }
}
