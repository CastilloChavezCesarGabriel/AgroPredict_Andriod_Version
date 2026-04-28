package com.agropredict.presentation.viewmodel.authentication;

import com.agropredict.application.visitor.IOperationResultVisitor;

public final class LoginResultStrategy implements IOperationResultVisitor {
    private final ILoginView view;

    public LoginResultStrategy(ILoginView view) {
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