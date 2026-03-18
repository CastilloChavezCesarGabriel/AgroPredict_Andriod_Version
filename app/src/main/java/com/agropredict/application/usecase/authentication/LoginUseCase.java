package com.agropredict.application.usecase.authentication;

import com.agropredict.application.PasswordHasher;
import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.result.OperationResult;
import com.agropredict.domain.entity.User;
import com.agropredict.domain.value.user.UserData;
import com.agropredict.domain.value.user.UserIdentity;
import com.agropredict.domain.visitor.IUserIdentityVisitor;
import com.agropredict.domain.visitor.IUserVisitor;

public final class LoginUseCase implements IUserVisitor, IUserIdentityVisitor {
    private final IUserRepository userRepository;
    private final ISessionRepository sessionRepository;
    private OperationResult operationResult;

    public LoginUseCase(IUserRepository userRepository, ISessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    public OperationResult authenticate(String email, String password) {
        String passwordHash = new PasswordHasher().hash(password);
        User user = userRepository.authenticate(email, passwordHash);
        if (user == null) return OperationResult.fail();
        user.accept(this);
        return operationResult;
    }

    @Override
    public void visit(UserIdentity identity, UserData data) {
        identity.accept(this);
    }

    @Override
    public void visitIdentity(String identifier, String fullName) {
        sessionRepository.save(identifier);
        this.operationResult = OperationResult.succeed(identifier);
    }
}