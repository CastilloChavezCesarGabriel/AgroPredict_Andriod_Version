package com.agropredict.application.usecase.authentication;

import com.agropredict.application.Hasher;
import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.result.OperationResult;
import com.agropredict.domain.LoginAttemptTracker;

public final class LoginUseCase {
    private final IUserRepository userRepository;
    private final ISessionRepository sessionRepository;
    private LoginAttemptTracker tracker = new LoginAttemptTracker();

    public LoginUseCase(IUserRepository userRepository, ISessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    public OperationResult authenticate(String email, String password) {
        long currentTime = System.currentTimeMillis();
        if (tracker.isBlocked(currentTime))
            return OperationResult.fail();
        String passwordHash = new Hasher().hash(password);
        String identifier = userRepository.authenticate(email, passwordHash);
        if (identifier == null) {
            tracker = tracker.fail(currentTime);
            return OperationResult.fail();
        }
        tracker = tracker.succeed();
        sessionRepository.save(identifier);
        return OperationResult.succeed(identifier);
    }
}