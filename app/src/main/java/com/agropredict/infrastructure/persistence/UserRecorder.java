package com.agropredict.infrastructure.persistence;

import android.content.ContentValues;
import com.agropredict.domain.value.user.Credentials;
import com.agropredict.domain.value.user.UserContact;
import com.agropredict.domain.value.user.UserData;
import com.agropredict.domain.value.user.UserIdentity;
import com.agropredict.domain.value.user.UserProfile;
import com.agropredict.domain.visitor.ICredentialsVisitor;
import com.agropredict.domain.visitor.IUserContactVisitor;
import com.agropredict.domain.visitor.IUserDataVisitor;
import com.agropredict.domain.visitor.IUserIdentityVisitor;
import com.agropredict.domain.visitor.IUserOccupationVisitor;
import com.agropredict.domain.visitor.IUserProfileVisitor;
import com.agropredict.domain.visitor.IUserVisitor;

public final class UserRecorder implements IUserVisitor, IUserDataVisitor,
        IUserProfileVisitor, IUserIdentityVisitor, ICredentialsVisitor,
        IUserContactVisitor, IUserOccupationVisitor {

    private final ContentValues values;

    public UserRecorder(ContentValues values) {
        this.values = values;
    }

    @Override
    public void visit(UserIdentity identity, UserData data) {
        identity.accept(this);
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
        values.put("occupation_id", occupationIdentifier);
    }

    @Override
    public void visitIdentity(String identifier, String fullName) {
        values.put("id", identifier);
        values.put("full_name", fullName);
    }

    @Override
    public void visit(String email, String passwordHash) {
        values.put("email", email);
        values.put("password_hash", passwordHash);
    }

    @Override
    public void visitContact(String username, String phoneNumber) {
        values.put("username", username);
        values.put("phone_number", phoneNumber);
    }

    @Override
    public void visitOccupation(String occupationIdentifier) {
        values.put("occupation_id", occupationIdentifier);
    }
}
