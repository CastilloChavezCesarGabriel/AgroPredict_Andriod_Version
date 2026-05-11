package com.agropredict.presentation.viewmodel.authentication;

import com.agropredict.application.authentication.request.RegistrationRequest;
import com.agropredict.application.authentication.usecase.RegisterUseCase;
import com.agropredict.application.catalog.ListCatalogUseCase;

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
        registerUseCase.register(request).accept(new RegistrationResultPresenter(view));
    }
}
