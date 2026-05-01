package com.agropredict.presentation.viewmodel.authentication;

import com.agropredict.application.request.user_registration.RegistrationRequest;
import com.agropredict.application.operation_result.RegistrationResult;
import com.agropredict.application.usecase.authentication.RegisterUseCase;
import com.agropredict.application.usecase.catalog.ListCatalogUseCase;

public final class RegisterViewModel {
    private final RegisterUseCase registerUseCase;
    private final IRegisterView view;

    public RegisterViewModel(RegisterUseCase registerUseCase, IRegisterView view) {
        this.registerUseCase = registerUseCase;
        this.view = view;
    }

    public void populate(ListCatalogUseCase listOccupationsUseCase) {
        view.populate(listOccupationsUseCase.list());
    }

    public void register(RegistrationRequest request) {
        RegistrationResult result = registerUseCase.register(request);
        result.accept(new RegistrationResultPresenter(view));
    }
}