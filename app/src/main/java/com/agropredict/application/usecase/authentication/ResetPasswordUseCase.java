package com.agropredict.application.usecase.authentication;

import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.operation_result.IUseCaseResult;
import com.agropredict.application.operation_result.SuccessfulOperation;
import com.agropredict.application.operation_result.FailedOperation;
import com.agropredict.application.service.IPasswordHasher;
import com.agropredict.domain.input_validation.PasswordValidator;
import com.agropredict.domain.input_validation.RuleValidationException;
import com.agropredict.domain.input_validation.ValidationGate;
import java.util.Objects;

public final class ResetPasswordUseCase {
    private final IUserRepository userRepository;
    private final IPasswordHasher passwordHasher;

    public ResetPasswordUseCase(IUserRepository userRepository, IPasswordHasher passwordHasher) {
        this.userRepository = Objects.requireNonNull(userRepository, "reset password use case requires a user repository");
        this.passwordHasher = Objects.requireNonNull(passwordHasher, "reset password use case requires a password hasher");
    }

    public IUseCaseResult reset(String email, String newPassword) {
        try {
            new PasswordValidator().check(newPassword, new ValidationGate());
        } catch (RuleValidationException invalid) {
            return new FailedOperation();
        }
        boolean updated = userRepository.reset(email, passwordHasher.hash(newPassword));
        return updated ? new SuccessfulOperation("Password updated") : new FailedOperation();
    }
}