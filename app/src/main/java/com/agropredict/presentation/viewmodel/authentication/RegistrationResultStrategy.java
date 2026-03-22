package com.agropredict.presentation.viewmodel.authentication;

import com.agropredict.application.visitor.IRegistrationResultVisitor;

public final class RegistrationResultStrategy implements IRegistrationResultVisitor {
    private final IRegisterView view;

    public RegistrationResultStrategy(IRegisterView view) {
        this.view = view;
    }

    @Override
    public void visit(boolean completed, String errorMessage) {
        if (completed) {
            view.notify("Registro exitoso");
            view.dismiss();
        } else {
            view.notify(errorMessage);
        }
    }
}