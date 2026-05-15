package com.agropredict.application.authentication.usecase;

import com.agropredict.application.factory.failure.IAuthenticationFailureFactory;
import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.operation_result.IUseCaseResult;
import com.agropredict.application.operation_result.RejectedOperation;
import com.agropredict.application.operation_result.SuccessfulOperation;
import com.agropredict.domain.authentication.gate.LoginRejectedException;
import com.agropredict.domain.authentication.failure.IAuthenticationFailure;
import com.agropredict.domain.user.ISessionSubject;
import java.util.Objects;

public final class LoginUseCase implements IAuthenticationResult {
    private final IUserRepository userRepository;
    private final ISessionRepository sessionRepository;
    private final IAuthenticationFailureFactory failureFactory;
    private final LoginGateFactory gateFactory;
    private final AttemptTracker tracker;

    public LoginUseCase(IUserRepository userRepository, ISessionRepository sessionRepository, IAuthenticationFailureFactory failureFactory) {
        this.userRepository = Objects.requireNonNull(userRepository, "login use case requires a user repository");
        this.sessionRepository = Objects.requireNonNull(sessionRepository, "login use case requires a session repository");
        this.failureFactory = Objects.requireNonNull(failureFactory, "login use case requires a failure factory");
        this.gateFactory = new LoginGateFactory(failureFactory);
        this.tracker = new AttemptTracker();
    }

    public IUseCaseResult login(String email, String password) {
        long currentTime = System.currentTimeMillis();
        try {
            tracker.evaluate(currentTime, gateFactory.create());
            return authenticate(email, password, currentTime);
        } catch (LoginRejectedException rejected) {
            return new RejectedOperation(rejected.getMessage());
        }
    }

    private IUseCaseResult authenticate(String email, String password, long currentTime) {
        ISessionSubject subject = userRepository.authenticate(email, password);
        boolean granted = subject.expose(new SessionAttempt(sessionRepository));
        return granted ? onAuthenticate(email) : onReject(currentTime);
    }

    @Override
    public IUseCaseResult onAuthenticate(String email) {
        tracker.succeed();
        return new SuccessfulOperation(email);
    }

    @Override
    public IUseCaseResult onReject(long currentTime) {
        tracker.fail(currentTime);
        try {
            tracker.evaluate(currentTime, gateFactory.create());
            return assemble(failureFactory.createIncorrectCredential());
        } catch (LoginRejectedException rejected) {
            return new RejectedOperation(rejected.getMessage());
        }
    }

    private RejectedOperation assemble(IAuthenticationFailure failure) {
        StringBuilder buffer = new StringBuilder();
        failure.encode(buffer::append);
        return new RejectedOperation(buffer.toString());
    }
}
