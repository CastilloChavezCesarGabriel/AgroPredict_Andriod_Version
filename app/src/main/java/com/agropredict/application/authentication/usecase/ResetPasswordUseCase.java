package com.agropredict.application.authentication.usecase;

import com.agropredict.application.factory.failure.IPasswordFailureFactory;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.operation_result.IUseCaseResult;
import com.agropredict.application.operation_result.FailedOperation;
import com.agropredict.application.operation_result.SuccessfulOperation;
import com.agropredict.application.service.IPasswordHasher;
import com.agropredict.application.input_validation.PasswordValidator;
import com.agropredict.domain.input_validation.gate.RuleValidationException;
import com.agropredict.domain.input_validation.gate.ValidationGate;
import java.util.Objects;

public final class ResetPasswordUseCase {
    private final IUserRepository userRepository;
    private final IPasswordHasher passwordHasher;
    private final IPasswordFailureFactory failureFactory;

    public ResetPasswordUseCase(IUserRepository userRepository, IPasswordHasher passwordHasher, IPasswordFailureFactory failureFactory) {
        this.userRepository = Objects.requireNonNull(userRepository, "reset password use case requires a user repository");
        this.passwordHasher = Objects.requireNonNull(passwordHasher, "reset password use case requires a password hasher");
        this.failureFactory = Objects.requireNonNull(failureFactory, "reset password use case requires a failure factory");
    }

    public IUseCaseResult reset(String email, String newPassword) {
        try {
            new PasswordValidator(failureFactory).check(newPassword, new ValidationGate());
        } catch (RuleValidationException invalid) {
            return new FailedOperation();
        }
        boolean updated = userRepository.reset(email, passwordHasher.hash(newPassword));
        return updated ? new SuccessfulOperation(email) : new FailedOperation();
    }
}
