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
            return OperationResult.reject("Account locked. Try again in a few minutes.");
        String identifier = userRepository.authenticate(email, password);
        if (identifier == null) {
            tracker = tracker.fail(currentTime);
            if (tracker.isExhausted())
                return OperationResult.reject("Too many attempts. Account locked for 5 minutes.");
            return OperationResult.reject("Incorrect credentials");
        }
        tracker = tracker.succeed();
        sessionRepository.save(identifier);
        return OperationResult.succeed(identifier);
    }
}