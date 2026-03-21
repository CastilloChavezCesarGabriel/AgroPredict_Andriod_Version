package com.agropredict.application.usecase.authentication;

import com.agropredict.application.PasswordHasher;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.result.OperationResult;
import com.agropredict.domain.validation.PasswordValidator;

public final class ResetPasswordUseCase {
    private final IUserRepository userRepository;

    public ResetPasswordUseCase(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public OperationResult reset(String email, String newPassword) {
        if (!userRepository.isRegistered(email))
            return OperationResult.fail();
        if (!new PasswordValidator().validate(newPassword))
            return OperationResult.fail();
        String newHash = new PasswordHasher().hash(newPassword);
        boolean updated = userRepository.reset(email, newHash);
        if (!updated) return OperationResult.fail();
        return OperationResult.succeed("Contraseña actualizada");
    }
}