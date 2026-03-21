package com.agropredict.application.usecase.authentication;

import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.result.OperationResult;
import com.agropredict.domain.LoginAttempt;

public final class LoginUseCase {
    private final IUserRepository userRepository;
    private final ISessionRepository sessionRepository;
    private LoginAttempt tracker = new LoginAttempt();

    public LoginUseCase(IUserRepository userRepository, ISessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    public OperationResult authenticate(String email, String password) {
        long currentTime = System.currentTimeMillis();
        if (tracker.isBlocked(currentTime))
            return OperationResult.reject("Cuenta bloqueada. Intenta en unos minutos.");
        String identifier = userRepository.authenticate(email, password);
        if (identifier == null) {
            tracker = tracker.fail(currentTime);
            if (tracker.isExhausted())
                return OperationResult.reject("Has agotado tus intentos. Cuenta bloqueada por 5 minutos.");
            return OperationResult.reject("Credenciales incorrectas");
        }
        tracker = tracker.succeed();
        sessionRepository.save(identifier);
        return OperationResult.succeed(identifier);
    }
}