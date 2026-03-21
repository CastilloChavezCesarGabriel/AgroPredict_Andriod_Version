package com.agropredict.presentation.viewmodel.home;

import com.agropredict.application.usecase.authentication.LogoutUseCase;

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
