package com.agropredict.presentation.viewmodel.authentication;

import com.agropredict.application.consumer.IOperationResultConsumer;

public final class LoginResultStrategy implements IOperationResultConsumer {

    private final ILoginView view;

    public LoginResultStrategy(ILoginView view) {
        this.view = view;
    }

    @Override
    public void visit(boolean completed, String resultIdentifier) {
        if (completed) {
            view.navigateToHome();
        } else {
            view.notify("Credenciales incorrectas");
        }
    }
}
