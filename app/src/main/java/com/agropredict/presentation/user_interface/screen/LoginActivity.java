package com.agropredict.presentation.user_interface.screen;

import android.os.Bundle;
import android.widget.EditText;
import com.agropredict.R;
import com.agropredict.application.factory.IAccessFactory;
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
        bind();
        initialize();
        listen();
    }

    private void bind() {
        emailInput = findViewById(R.id.etEmail);
        passwordInput = findViewById(R.id.etPassword);
    }

    private void initialize() {
        IAccessFactory factory = (IAccessFactory) getApplication();
        LoginUseCase loginUseCase = new LoginUseCase(
                factory.createUserRepository(), factory.createSessionRepository());
        viewModel = new LoginViewModel(loginUseCase, this);
        auditLogger = factory.createAuditLogger();
        new CheckSessionUseCase(factory.createSessionRepository()).check((identifier, occupation) -> {
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

    @Override
    public void reject() {
        notify(getString(R.string.login_failure));
    }
}