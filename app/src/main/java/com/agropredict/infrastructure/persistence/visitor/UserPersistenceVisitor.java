package com.agropredict.infrastructure.persistence.visitor;

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
import com.agropredict.infrastructure.persistence.IRow;

public final class UserPersistenceVisitor implements IUserVisitor, IUserDataVisitor,
        IUserProfileVisitor, IUserIdentityVisitor, ICredentialsVisitor,
        IUserContactVisitor {

    private final IRow row;

    public UserPersistenceVisitor(IRow row) {
        this.row = row;
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
        row.record("occupation_id", occupationIdentifier);
    }

    @Override
    public void visitIdentity(String identifier, String fullName) {
        row.record("id", identifier);
        row.record("full_name", fullName);
    }

    @Override
    public void visit(String email, String passwordHash) {
        row.record("email", email);
        row.record("password_hash", passwordHash);
    }

    @Override
    public void visitContact(String username, String phoneNumber) {
        row.record("username", username);
        row.record("phone_number", phoneNumber);
    }
}
