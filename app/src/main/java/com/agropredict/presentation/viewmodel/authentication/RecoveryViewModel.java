package com.agropredict.presentation.viewmodel.authentication;

import com.agropredict.application.result.OperationResult;
import com.agropredict.application.usecase.authentication.ResetPasswordUseCase;

public final class RecoveryViewModel {
    private final ResetPasswordUseCase resetUseCase;
    private final IRecoveryView view;

    public RecoveryViewModel(ResetPasswordUseCase resetUseCase, IRecoveryView view) {
        this.resetUseCase = resetUseCase;
        this.view = view;
    }

    public void reset(String email, String newPassword, String confirmation) {
        if (!newPassword.equals(confirmation)) {
            view.notify("Las contraseñas no coinciden");
            return;
        }
        OperationResult result = resetUseCase.reset(email, newPassword);
        result.accept(new RecoveryResultStrategy(view));
    }
}
