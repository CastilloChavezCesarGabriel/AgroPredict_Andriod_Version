package com.agropredict.application.usecase.authentication;

import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.operation_result.OperationResult;
import com.agropredict.domain.LoginAttempt;
import com.agropredict.domain.Session;

public final class LoginUseCase {
    private final IUserRepository userRepository;
    private final ISessionRepository sessionRepository;
    private LoginAttempt tracker = new LoginAttempt();

    public LoginUseCase(IUserRepository userRepository, ISessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    public OperationResult login(String email, String password) {
        long currentTime = System.currentTimeMillis();
        if (tracker.isBlocked(currentTime)) {
            return OperationResult.reject("Account locked. Try again in a few minutes.");
        }

        Session session = userRepository.authenticate(email, password);
        if (session == null) return reject(currentTime);
        tracker = tracker.succeed();
        sessionRepository.save(session);
        return OperationResult.succeed("User is authenticated");
    }

    private OperationResult reject(long currentTime) {
        tracker = tracker.fail(currentTime);
        if (tracker.isExhausted())
            return OperationResult.reject("Too many attempts. Account locked for 5 minutes.");
        return OperationResult.reject("Incorrect credentials");
    }
}