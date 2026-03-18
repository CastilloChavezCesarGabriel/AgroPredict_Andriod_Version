package com.agropredict.presentation.viewmodel.authentication;

import com.agropredict.application.usecase.authentication.LoginUseCase;
import com.agropredict.application.result.OperationResult;

public final class LoginViewModel {
    private final LoginUseCase loginUseCase;
    private ILoginView view;

    public LoginViewModel(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    public void bind(ILoginView view) {
        this.view = view;
    }

    public void authenticate(String email, String password) {
        OperationResult result = loginUseCase.authenticate(email, password);
        if (view != null) {
            result.accept(new LoginResultStrategy(view));
        }
    }
}
