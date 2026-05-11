package com.agropredict.presentation.viewmodel.authentication;

import com.agropredict.application.operation_result.IOperationResult;

public final class RegistrationResultPresenter implements IOperationResult {
    private final IRegisterView view;

    public RegistrationResultPresenter(IRegisterView view) {
        this.view = view;
    }

    @Override
    public void onSucceed(String value) {
        view.confirm();
        view.dismiss();
    }

    @Override
    public void onFail() {
        view.notify("Registration failed");
    }

    @Override
    public void onReject(String reason) {
        view.notify(reason);
    }
}
