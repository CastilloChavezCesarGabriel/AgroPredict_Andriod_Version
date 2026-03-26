package com.agropredict.application.usecase.authentication;

import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.request.RegistrationException;
import com.agropredict.application.request.RegistrationRequest;
import com.agropredict.application.result.RegistrationResult;
import com.agropredict.application.service.IPasswordHasher;

public final class RegisterUseCase {
    private final IUserRepository userRepository;
    private final ICatalogRepository occupationCatalog;

    public RegisterUseCase(IUserRepository userRepository, ICatalogRepository occupationCatalog) {
        this.userRepository = userRepository;
        this.occupationCatalog = occupationCatalog;
    }

    public RegistrationResult register(RegistrationRequest request, IPasswordHasher hasher) {
        try {
            request.validate(userRepository);
            userRepository.store(request.compile(hasher, occupationCatalog));
            return RegistrationResult.succeed();
        } catch (RegistrationException exception) {
            return RegistrationResult.fail(exception.getMessage());
        }
    }
}
