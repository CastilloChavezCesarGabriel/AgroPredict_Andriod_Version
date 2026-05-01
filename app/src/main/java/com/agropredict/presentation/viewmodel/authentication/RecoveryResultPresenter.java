package com.agropredict.presentation.viewmodel.authentication;

import com.agropredict.application.visitor.IOperationResultVisitor;

public final class RecoveryResultPresenter implements IOperationResultVisitor {
    private final IRecoveryView view;

    public RecoveryResultPresenter(IRecoveryView view) {
        this.view = view;
    }

    @Override
    public void visit(boolean success, String message) {
        if (success) {
            view.confirm();
            view.dismiss();
        } else {
            view.warn();
        }
    }
}