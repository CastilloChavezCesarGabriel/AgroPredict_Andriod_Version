package com.agropredict.presentation.user_interface;

import android.os.Bundle;
import android.widget.EditText;
import com.agropredict.AgroPredictApplication;
import com.agropredict.R;
import com.agropredict.application.usecase.authentication.LoginUseCase;
import com.agropredict.presentation.viewmodel.authentication.ILoginView;
import com.agropredict.presentation.viewmodel.authentication.LoginViewModel;

public final class LoginActivity extends BaseActivity implements ILoginView {

    private LoginViewModel viewModel;
    private EditText emailInput;
    private EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        compose();
        bind();
    }

    private void compose() {
        AgroPredictApplication application = (AgroPredictApplication) getApplication();
        application.provide(factory -> {
            LoginUseCase loginUseCase = new LoginUseCase(factory.createUserRepository(), factory.createSessionRepository());
            viewModel = new LoginViewModel(loginUseCase);
            viewModel.bind(this);
        });
    }

    private void bind() {
        emailInput = findViewById(R.id.etEmail);
        passwordInput = findViewById(R.id.etPassword);
        findViewById(R.id.btnLogin).setOnClickListener(clickedView -> onLoginClicked());
        findViewById(R.id.tvGoToRegister).setOnClickListener(clickedView -> navigateToRegister());
    }

    private void onLoginClicked() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString();
        if (email.isEmpty() || password.isEmpty()) {
            notify(getString(R.string.field_required));
            return;
        }
        viewModel.authenticate(email, password);
    }

    @Override
    public void navigateToHome() {
        redirect(HomeActivity.class);
    }

    @Override
    public void navigateToRegister() {
        navigate(RegisterActivity.class);
    }
}
