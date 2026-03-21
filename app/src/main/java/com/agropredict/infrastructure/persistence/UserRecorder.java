package com.agropredict.infrastructure.persistence;

import com.agropredict.domain.component.user.Credential;
import com.agropredict.domain.component.user.UserContact;
import com.agropredict.domain.component.user.UserData;
import com.agropredict.domain.component.user.UserIdentity;
import com.agropredict.domain.component.user.UserProfile;
import com.agropredict.domain.visitor.user.ICredentialsVisitor;
import com.agropredict.domain.visitor.user.IUserContactVisitor;
import com.agropredict.domain.visitor.user.IUserDataVisitor;
import com.agropredict.domain.visitor.user.IUserIdentityVisitor;
import com.agropredict.domain.visitor.user.IUserProfileVisitor;
import com.agropredict.domain.visitor.user.IUserVisitor;

public final class UserRecorder implements IUserVisitor, IUserDataVisitor,
        IUserProfileVisitor, IUserIdentityVisitor, ICredentialsVisitor,
        IUserContactVisitor {

    private final IRecord record;

    public UserRecorder(IRecord record) {
        this.record = record;
    }

    @Override
    public void visit(UserIdentity identity, UserData data) {
        identity.accept(this);
        data.accept(this);
    }

    @Override
    public void visit(Credential credential, UserProfile profile) {
        credential.accept(this);
        if (profile != null) profile.accept(this);
    }

    @Override
    public void visit(UserContact contact, String occupationIdentifier) {
        if (contact != null) contact.accept(this);
        record.record("occupation_id", occupationIdentifier);
    }

    @Override
    public void visitIdentity(String identifier, String fullName) {
        record.record("id", identifier);
        record.record("full_name", fullName);
    }

    @Override
    public void visit(String email, String passwordHash) {
        record.record("email", email);
        record.record("password_hash", passwordHash);
    }

    @Override
    public void visitContact(String username, String phoneNumber) {
        record.record("username", username);
        record.record("phone_number", phoneNumber);
    }
}
