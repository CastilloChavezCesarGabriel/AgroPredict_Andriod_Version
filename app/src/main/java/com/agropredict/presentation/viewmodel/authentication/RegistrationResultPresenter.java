package com.agropredict.presentation.viewmodel.authentication;

import com.agropredict.application.visitor.IRegistrationResultVisitor;

public final class RegistrationResultPresenter implements IRegistrationResultVisitor {
    private final IRegisterView view;

    public RegistrationResultPresenter(IRegisterView view) {
        this.view = view;
    }

    @Override
    public void visit(boolean completed, String errorMessage) {
        if (completed) {
            view.confirm();
            view.dismiss();
        } else {
            view.notify(errorMessage);
        }
    }
}