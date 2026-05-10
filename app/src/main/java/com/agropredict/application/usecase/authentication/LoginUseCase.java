package com.agropredict.application.usecase.authentication;

import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.operation_result.IUseCaseResult;
import com.agropredict.application.operation_result.SuccessfulOperation;
import com.agropredict.application.operation_result.RejectedOperation;
import com.agropredict.domain.authentication.ILoginAttempt;
import com.agropredict.domain.authentication.InitialAttempt;
import com.agropredict.domain.authentication.LoginGate;
import com.agropredict.domain.authentication.LoginRejectedException;
import com.agropredict.domain.user.ISessionSubject;

import java.util.Objects;

public final class LoginUseCase implements IAuthenticationResult {
    private final IUserRepository userRepository;
    private final ISessionRepository sessionRepository;
    private ILoginAttempt attempt = new InitialAttempt();

    public LoginUseCase(IUserRepository userRepository, ISessionRepository sessionRepository) {
        this.userRepository = Objects.requireNonNull(userRepository, "login use case requires a user repository");
        this.sessionRepository = Objects.requireNonNull(sessionRepository, "login use case requires a session repository");
    }

    public IUseCaseResult login(String email, String password) {
        long currentTime = System.currentTimeMillis();
        try {
            attempt.evaluate(currentTime, new LoginGate());
            return authenticate(email, password, currentTime);
        } catch (LoginRejectedException rejected) {
            return new RejectedOperation(rejected.getMessage());
        }
    }

    private IUseCaseResult authenticate(String email, String password, long currentTime) {
        ISessionSubject subject = userRepository.authenticate(email, password);
        boolean granted = subject.expose(new SessionAttempt(sessionRepository));
        return granted ? onAuthenticate() : onReject(currentTime);
    }

    @Override
    public IUseCaseResult onAuthenticate() {
        attempt = attempt.succeed();
        return new SuccessfulOperation("User is authenticated");
    }

    @Override
    public IUseCaseResult onReject(long currentTime) {
        attempt = attempt.fail(currentTime);
        try {
            attempt.evaluate(currentTime, new LoginGate());
            return new RejectedOperation("Incorrect credentials");
        } catch (LoginRejectedException rejected) {
            return new RejectedOperation(rejected.getMessage());
        }
    }
}
