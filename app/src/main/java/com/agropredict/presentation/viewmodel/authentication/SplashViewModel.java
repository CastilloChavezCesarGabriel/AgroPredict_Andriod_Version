package com.agropredict.presentation.viewmodel.authentication;

import com.agropredict.application.usecase.authentication.CheckSessionUseCase;
import com.agropredict.application.consumer.ISessionResultConsumer;

public final class SplashViewModel {

    private final CheckSessionUseCase checkSessionUseCase;

    public SplashViewModel(CheckSessionUseCase checkSessionUseCase) {
        this.checkSessionUseCase = checkSessionUseCase;
    }

    public void check(ISessionResultConsumer consumer) {
        checkSessionUseCase.check(consumer);
    }
}