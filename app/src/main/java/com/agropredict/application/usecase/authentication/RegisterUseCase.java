package com.agropredict.application.usecase.authentication;

import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.request.user_registration.RegistrationException;
import com.agropredict.application.request.user_registration.RegistrationRequest;
import com.agropredict.application.operation_result.RegistrationResult;
import com.agropredict.application.service.IPasswordHasher;

public final class RegisterUseCase {
    private final IUserRepository userRepository;
    private final IPasswordHasher passwordHasher;

    public RegisterUseCase(IUserRepository userRepository, IPasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    public RegistrationResult register(RegistrationRequest request) {
        try {
            request.validate();
            userRepository.register(request, passwordHasher);
            return RegistrationResult.succeed();
        } catch (RegistrationException exception) {
            return RegistrationResult.fail(exception.getMessage());
        }
    }
}