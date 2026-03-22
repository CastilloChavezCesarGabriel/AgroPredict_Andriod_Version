package com.agropredict.presentation.user_interface.holder;
import com.agropredict.presentation.user_interface.input.CredentialInput;
import com.agropredict.presentation.user_interface.input.SpinnerPopulator;

import android.app.Activity;
import android.widget.EditText;
import android.widget.Spinner;
import com.agropredict.R;
import com.agropredict.application.request.data.Account;
import com.agropredict.application.request.data.Registrant;
import com.agropredict.application.request.data.Profile;
import com.agropredict.application.request.RegistrationRequest;

public final class RegisterViewHolder {
    private final EditText fullNameInput;
    private final EditText phoneInput;
    private final EditText usernameInput;
    private final Spinner occupationSpinner;
    private final CredentialInput credentialGroup;

    public RegisterViewHolder(Activity activity) {
        this.fullNameInput = activity.findViewById(R.id.etFullName);
        this.phoneInput = activity.findViewById(R.id.etPhone);
        this.usernameInput = activity.findViewById(R.id.etUsername);
        this.occupationSpinner = activity.findViewById(R.id.spnOccupation);
        this.credentialGroup = new CredentialInput(activity);
    }

    public boolean match() {
        return credentialGroup.match();
    }

    public RegistrationRequest collect() {
        String fullName = fullNameInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        Registrant personal = new Registrant(fullName, phone);
        String username = usernameInput.getText().toString().trim();
        String occupation = occupationSpinner.getSelectedItem().toString();
        Profile profile = new Profile(username, occupation);
        Account account = new Account(credentialGroup.collect(), profile);
        return new RegistrationRequest(personal, account);
    }

    public void populate(java.util.List<String> occupations) {
        SpinnerPopulator.populate(occupationSpinner, occupations);
    }
}
