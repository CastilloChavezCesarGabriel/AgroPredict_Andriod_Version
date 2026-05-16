package com.agropredict.presentation.user_interface.screen;

import android.os.Bundle;
import com.agropredict.R;
import com.agropredict.application.factory.IAccessFactory;
import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.application.service.IAuditLogger;
import com.agropredict.application.authentication.usecase.CheckSessionUseCase;
import com.agropredict.application.authentication.usecase.LoginUseCase;
import com.agropredict.presentation.user_interface.questionnaire_input.LoginCredentialInput;
import com.agropredict.presentation.viewmodel.authentication.AndroidAuthenticationFailureFactory;
import com.agropredict.presentation.viewmodel.authentication.ILoginView;
import com.agropredict.presentation.viewmodel.authentication.LoginViewModel;

public final class LoginActivity extends BaseActivity implements ILoginView {
    private LoginViewModel viewModel;
    private LoginCredentialInput credentialInput;
    private IAuditLogger auditLogger;
    private ISessionRepository sessionRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bind();
        initialize();
        listen();
    }

    private void bind() {
        credentialInput = new LoginCredentialInput(this);
    }

    private void initialize() {
        IAccessFactory factory = (IAccessFactory) getApplication();
        sessionRepository = factory.createSessionRepository();
        LoginUseCase loginUseCase = new LoginUseCase(
                factory.createUserRepository(), sessionRepository,
                new AndroidAuthenticationFailureFactory(this));
        viewModel = new LoginViewModel(loginUseCase, this);
        auditLogger = factory.createAuditLogger();
        new CheckSessionUseCase(sessionRepository).check((identifier, occupation) -> {
            if (identifier != null) redirect(HomeActivity.class);
        });
    }

    private void listen() {
        findViewById(R.id.btnLogin).setOnClickListener(view -> authenticate());
        findViewById(R.id.tvGoToRegister).setOnClickListener(view -> navigate(RegisterActivity.class));
        findViewById(R.id.tvGoToRecovery).setOnClickListener(view -> navigate(RecoveryActivity.class));
    }

    private void authenticate() {
        credentialInput.submit(viewModel::login, () -> notify(getString(R.string.field_required)));
    }

    @Override
    public void proceed(String email) {
        notify(getString(R.string.login_success, email));
        sessionRepository.recall().report((identifier, occupation) ->
                auditLogger.log(identifier, "LOGIN"));
        redirect(HomeActivity.class);
    }

    @Override
    public void reject() {
        notify(getString(R.string.login_failure));
    }
}
