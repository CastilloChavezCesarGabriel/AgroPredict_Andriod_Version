package com.agropredict.application.usecase.authentication;

import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.result.RegistrationResult;
import com.agropredict.domain.entity.User;
import com.agropredict.domain.value.user.Credentials;
import com.agropredict.domain.value.user.UserContact;
import com.agropredict.domain.value.user.UserData;
import com.agropredict.domain.value.user.UserIdentity;
import com.agropredict.domain.value.user.UserProfile;
import com.agropredict.domain.visitor.ICredentialsVisitor;
import com.agropredict.domain.visitor.IUserContactVisitor;
import com.agropredict.domain.visitor.IUserDataVisitor;
import com.agropredict.domain.visitor.IUserProfileVisitor;
import com.agropredict.domain.visitor.IUserVisitor;

public final class RegisterUseCase implements IUserVisitor, IUserDataVisitor,
        IUserProfileVisitor, ICredentialsVisitor, IUserContactVisitor {
    private final IUserRepository userRepository;
    private String duplicateMessage;

    public RegisterUseCase(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public RegistrationResult register(User user) {
        this.duplicateMessage = null;
        user.accept(this);
        if (hasDuplicate()) {
            return RegistrationResult.fail(duplicateMessage);
        }
        userRepository.store(user);
        return RegistrationResult.succeed();
    }

    @Override
    public void visit(UserIdentity identity, UserData data) {
        data.accept(this);
    }

    @Override
    public void visit(Credentials credentials, UserProfile profile) {
        credentials.accept(this);
        if (profile != null) profile.accept(this);
    }

    @Override
    public void visit(UserContact contact, String occupationIdentifier) {
        if (contact != null) contact.accept(this);
    }

    @Override
    public void visit(String email, String passwordHash) {
        if (userRepository.isRegistered(email)) {
            duplicateMessage = "El correo ya esta registrado";
        }
    }

    @Override
    public void visitContact(String username, String phoneNumber) {
        if (duplicateMessage != null) return;
        if (userRepository.isTaken(username)) {
            duplicateMessage = "El nombre de usuario ya existe";
        }
    }

    private boolean hasDuplicate() {
        return duplicateMessage != null;
    }
}
