package com.agropredict.application.usecase.authentication;

import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.operation_result.OperationResult;
import com.agropredict.application.service.IPasswordHasher;
import com.agropredict.domain.input_validation.PasswordValidator;

public final class ResetPasswordUseCase {
    private final IUserRepository userRepository;
    private final IPasswordHasher passwordHasher;

    public ResetPasswordUseCase(IUserRepository userRepository, IPasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    public OperationResult reset(String email, String newPassword) {
        if (!isEligible(email, newPassword)) return OperationResult.fail();
        boolean updated = userRepository.reset(email, passwordHasher.hash(newPassword));
        return updated ? OperationResult.succeed("Password updated") : OperationResult.fail();
    }

    private boolean isEligible(String email, String password) {
        return userRepository.isRegistered(email) && new PasswordValidator().isValid(password);
    }
}
