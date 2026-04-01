package com.agropredict.presentation.user_interface.screen;

import android.os.Bundle;
import android.widget.EditText;
import com.agropredict.AgroPredictApplication;
import com.agropredict.R;
import com.agropredict.application.usecase.authentication.ResetPasswordUseCase;
import com.agropredict.presentation.viewmodel.authentication.IRecoveryView;
import com.agropredict.presentation.viewmodel.authentication.RecoveryViewModel;

public final class RecoveryActivity extends BaseActivity implements IRecoveryView {
    private EditText emailField;
    private EditText passwordField;
    private EditText confirmationField;
    private RecoveryViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery);
        emailField = findViewById(R.id.etRecoveryEmail);
        passwordField = findViewById(R.id.etNewPassword);
        confirmationField = findViewById(R.id.etConfirmPassword);
        ((AgroPredictApplication) getApplication()).provide(factory -> {
            ResetPasswordUseCase useCase = new ResetPasswordUseCase(factory.createUserRepository(), factory.createPasswordHasher());
            viewModel = new RecoveryViewModel(useCase, this);
        });
        findViewById(R.id.btnReset).setOnClickListener(view -> reset());
        findViewById(R.id.tvBackToLogin).setOnClickListener(view -> dismiss());
    }

    private void reset() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString();
        String confirmation = confirmationField.getText().toString();
        if (!password.equals(confirmation)) {
            notify(getString(R.string.passwords_mismatch));
            return;
        }
        viewModel.reset(email, password);
    }

    @Override
    public void dismiss() {
        finish();
    }
}
