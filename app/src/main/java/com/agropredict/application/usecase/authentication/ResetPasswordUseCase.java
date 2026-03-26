package com.agropredict.application.usecase.authentication;

import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.result.OperationResult;
import com.agropredict.application.service.IPasswordHasher;
import com.agropredict.domain.validation.PasswordValidator;

public final class ResetPasswordUseCase {
    private final IUserRepository userRepository;
    private final IPasswordHasher passwordHasher;

    public ResetPasswordUseCase(IUserRepository userRepository, IPasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    public OperationResult reset(String email, String newPassword) {
        if (!userRepository.isRegistered(email))
            return OperationResult.fail();
        if (!new PasswordValidator().isValid(newPassword))
            return OperationResult.fail();
        String newHash = passwordHasher.hash(newPassword);
        boolean updated = userRepository.reset(email, newHash);
        if (!updated) return OperationResult.fail();
        return OperationResult.succeed("Password updated");
    }
}
