package com.agropredict.presentation.viewmodel.authentication;

import com.agropredict.application.operation_result.IUseCaseResult;
import com.agropredict.application.usecase.authentication.ResetPasswordUseCase;

public final class RecoveryViewModel {
    private final ResetPasswordUseCase resetUseCase;
    private final IRecoveryView view;

    public RecoveryViewModel(ResetPasswordUseCase resetUseCase, IRecoveryView view) {
        this.resetUseCase = resetUseCase;
        this.view = view;
    }

    public void reset(String email, String newPassword) {
        IUseCaseResult result = resetUseCase.reset(email, newPassword);
        result.accept(new RecoveryResultPresenter(view));
    }
}