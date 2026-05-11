package com.agropredict.presentation.viewmodel.home_dashboard;

import com.agropredict.application.authentication.usecase.LogoutUseCase;

public final class HomeViewModel {
    private final LogoutUseCase logoutUseCase;
    private final IHomeView view;

    public HomeViewModel(LogoutUseCase logoutUseCase, IHomeView view) {
        this.logoutUseCase = logoutUseCase;
        this.view = view;
    }

    public void logout() {
        logoutUseCase.logout();
        view.logout();
    }
}