package com.agropredict.application.authentication.usecase;

import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.authentication.request.RegistrationException;
import com.agropredict.application.authentication.request.RegistrationRequest;
import com.agropredict.application.operation_result.IUseCaseResult;
import com.agropredict.application.operation_result.SuccessfulOperation;
import com.agropredict.application.operation_result.RejectedOperation;
import com.agropredict.domain.input_validation.RuleValidationException;
import java.util.Objects;

public final class RegisterUseCase {
    private final IUserRepository userRepository;
    private final ICatalogRepository occupationCatalog;

    public RegisterUseCase(IUserRepository userRepository, ICatalogRepository occupationCatalog) {
        this.userRepository = Objects.requireNonNull(userRepository, "register use case requires a user repository");
        this.occupationCatalog = Objects.requireNonNull(occupationCatalog, "register use case requires an occupation catalog");
    }

    public IUseCaseResult register(RegistrationRequest request) {
        try {
            request.validate();
            userRepository.register(request, occupationCatalog);
            return new SuccessfulOperation("User registered");
        } catch (RuleValidationException | RegistrationException exception) {
            return new RejectedOperation(exception.getMessage());
        }
    }
}