package com.agropredict.application.usecase.authentication;

import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.request.user_registration.RegistrationException;
import com.agropredict.application.request.user_registration.RegistrationRequest;
import com.agropredict.application.operation_result.RegistrationResult;

public final class RegisterUseCase {
    private final IUserRepository userRepository;
    private final ICatalogRepository occupationCatalog;

    public RegisterUseCase(IUserRepository userRepository, ICatalogRepository occupationCatalog) {
        this.userRepository = userRepository;
        this.occupationCatalog = occupationCatalog;
    }

    public RegistrationResult register(RegistrationRequest request) {
        try {
            request.validate();
            userRepository.register(request, occupationCatalog);
            return RegistrationResult.succeed();
        } catch (RegistrationException exception) {
            return RegistrationResult.fail(exception.getMessage());
        }
    }
}
