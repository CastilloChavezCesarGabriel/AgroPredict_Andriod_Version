package com.agropredict.presentation.viewmodel.authentication;

import com.agropredict.application.usecase.authentication.CheckSessionUseCase;
import com.agropredict.application.visitor.ISessionResultVisitor;

public final class SplashViewModel {

    private final CheckSessionUseCase checkSessionUseCase;

    public SplashViewModel(CheckSessionUseCase checkSessionUseCase) {
        this.checkSessionUseCase = checkSessionUseCase;
    }

    public void check(ISessionResultVisitor consumer) {
        checkSessionUseCase.check(consumer);
    }
}