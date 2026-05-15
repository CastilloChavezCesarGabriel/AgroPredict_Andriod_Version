package com.agropredict.presentation.viewmodel.authentication;

import com.agropredict.application.operation_result.IOperationResult;

public final class RecoveryResultPresenter implements IOperationResult {
    private final IRecoveryView view;

    public RecoveryResultPresenter(IRecoveryView view) {
        this.view = view;
    }

    @Override
    public void onSucceed(String value) {
        view.confirm(value);
        view.dismiss();
    }

    @Override
    public void onFail() {
        view.warn();
    }

    @Override
    public void onReject(String reason) {
        view.warn();
    }
}