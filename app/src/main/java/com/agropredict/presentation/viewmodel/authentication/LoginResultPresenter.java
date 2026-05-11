package com.agropredict.presentation.viewmodel.authentication;

import com.agropredict.application.operation_result.IOperationResult;

public final class LoginResultPresenter implements IOperationResult {
    private final ILoginView view;

    public LoginResultPresenter(ILoginView view) {
        this.view = view;
    }

    @Override
    public void onSucceed(String value) {
        view.proceed();
    }

    @Override
    public void onFail() {
        view.reject();
    }

    @Override
    public void onReject(String reason) {
        view.notify(reason);
    }
}