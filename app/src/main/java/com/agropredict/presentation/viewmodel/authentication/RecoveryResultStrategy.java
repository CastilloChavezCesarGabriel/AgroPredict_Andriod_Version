package com.agropredict.presentation.viewmodel.authentication;

import com.agropredict.application.visitor.IOperationResultVisitor;

public final class RecoveryResultStrategy implements IOperationResultVisitor {
    private final IRecoveryView view;

    public RecoveryResultStrategy(IRecoveryView view) {
        this.view = view;
    }

    @Override
    public void visit(boolean success, String message) {
        if (success) {
            view.notify("Contraseña actualizada exitosamente");
            view.dismiss();
        } else {
            view.notify("No se pudo actualizar. Verifica tu correo.");
        }
    }
}
