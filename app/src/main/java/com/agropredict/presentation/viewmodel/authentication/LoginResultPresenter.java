package com.agropredict.presentation.viewmodel.authentication;

import com.agropredict.application.visitor.IOperationResultVisitor;

public final class LoginResultPresenter implements IOperationResultVisitor {
    private final ILoginView view;

    public LoginResultPresenter(ILoginView view) {
        this.view = view;
    }

    @Override
    public void visit(boolean completed, String resultIdentifier) {
        if (completed) {
            view.proceed();
        } else if (resultIdentifier != null) {
            view.notify(resultIdentifier);
        } else {
            view.reject();
        }
    }
}