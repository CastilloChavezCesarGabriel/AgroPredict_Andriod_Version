package com.agropredict.presentation.user_interface.screen;

import android.os.Bundle;
import android.widget.EditText;
import com.agropredict.R;
import com.agropredict.application.factory.IAccessFactory;
import com.agropredict.application.authentication.usecase.ResetPasswordUseCase;
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
        bind();
        initialize();
        listen();
    }

    private void bind() {
        emailField = findViewById(R.id.etRecoveryEmail);
        passwordField = findViewById(R.id.etNewPassword);
        confirmationField = findViewById(R.id.etConfirmPassword);
    }

    private void initialize() {
        IAccessFactory factory = (IAccessFactory) getApplication();
        ResetPasswordUseCase useCase = new ResetPasswordUseCase(
                factory.createUserRepository(), factory.createPasswordHasher());
        viewModel = new RecoveryViewModel(useCase, this);
    }

    private void listen() {
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

    @Override
    public void confirm() {
        notify(getString(R.string.password_updated));
    }

    @Override
    public void warn() {
        notify(getString(R.string.recovery_failure));
    }
}