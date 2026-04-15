package com.agropredict.presentation.user_interface.screen;

import android.os.Bundle;
import android.widget.EditText;
import com.agropredict.AgroPredictApplication;
import com.agropredict.R;
import com.agropredict.application.IRepositoryFactory;
import com.agropredict.application.service.IAuditLogger;
import com.agropredict.application.usecase.authentication.CheckSessionUseCase;
import com.agropredict.application.usecase.authentication.LoginUseCase;
import com.agropredict.presentation.viewmodel.authentication.ILoginView;
import com.agropredict.presentation.viewmodel.authentication.LoginViewModel;

public final class LoginActivity extends BaseActivity implements ILoginView {
    private LoginViewModel viewModel;
    private EditText emailInput;
    private EditText passwordInput;
    private IAuditLogger auditLogger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailInput = findViewById(R.id.etEmail);
        passwordInput = findViewById(R.id.etPassword);
        ((AgroPredictApplication) getApplication()).provide(this::initialize);
        listen();
    }

    private void initialize(IRepositoryFactory factory) {
        LoginUseCase useCase = new LoginUseCase(
                factory.createUserRepository(), factory.createSessionRepository());
        viewModel = new LoginViewModel(useCase, this);
        auditLogger = factory.createAuditLogger();
        CheckSessionUseCase session = new CheckSessionUseCase(factory.createSessionRepository());
        session.check((identifier, occupation) -> {
            if (identifier != null) redirect(HomeActivity.class);
        });
    }

    private void listen() {
        findViewById(R.id.btnLogin).setOnClickListener(view -> authenticate());
        findViewById(R.id.tvGoToRegister).setOnClickListener(view -> navigate(RegisterActivity.class));
        findViewById(R.id.tvGoToRecovery).setOnClickListener(view -> navigate(RecoveryActivity.class));
    }

    private void authenticate() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString();
        if (email.isEmpty() || password.isEmpty()) {
            notify(getString(R.string.field_required));
            return;
        }
        viewModel.login(email, password);
    }

    @Override
    public void proceed() {
        auditLogger.log(null, "LOGIN");
        redirect(HomeActivity.class);
    }
}
