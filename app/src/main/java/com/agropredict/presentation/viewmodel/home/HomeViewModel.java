package com.agropredict.presentation.viewmodel.home;

import com.agropredict.application.usecase.authentication.LogoutUseCase;

public final class HomeViewModel {
    private final LogoutUseCase logoutUseCase;
    private IHomeView view;

    public HomeViewModel(LogoutUseCase logoutUseCase) {
        this.logoutUseCase = logoutUseCase;
    }

    public void bind(IHomeView view) {
        this.view = view;
    }

    public void logout() {
        logoutUseCase.execute();
        if (view != null) {
            view.navigateToLogin();
        }
    }
}