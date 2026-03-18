package com.agropredict.presentation.viewmodel.authentication;

import com.agropredict.application.consumer.IRegistrationResultConsumer;

public final class RegistrationResultStrategy implements IRegistrationResultConsumer {

    private final IRegisterView view;

    public RegistrationResultStrategy(IRegisterView view) {
        this.view = view;
    }

    @Override
    public void visit(boolean completed, String errorMessage) {
        if (completed) {
            view.notify("Registro exitoso");
            view.navigateToLogin();
        } else {
            view.notify(errorMessage);
        }
    }
}
