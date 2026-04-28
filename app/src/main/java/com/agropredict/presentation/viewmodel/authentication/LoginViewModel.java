package com.agropredict.presentation.viewmodel.authentication;

import com.agropredict.application.usecase.authentication.LoginUseCase;
import com.agropredict.application.operation_result.OperationResult;

public final class LoginViewModel {
    private final LoginUseCase loginUseCase;
    private final ILoginView view;

    public LoginViewModel(LoginUseCase loginUseCase, ILoginView view) {
        this.loginUseCase = loginUseCase;
        this.view = view;
    }

    public void login(String email, String password) {
        OperationResult result = loginUseCase.login(email, password);
        result.accept(new LoginResultStrategy(view));
    }
}