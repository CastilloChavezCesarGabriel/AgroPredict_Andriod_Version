package com.agropredict.infrastructure.persistence.visitor;

import com.agropredict.domain.user.visitor.ICredentialConsumer;
import com.agropredict.domain.user.visitor.IOccupationConsumer;
import com.agropredict.domain.user.visitor.IPhoneConsumer;
import com.agropredict.domain.user.visitor.IUserIdentityConsumer;
import com.agropredict.domain.user.visitor.IUsernameConsumer;
import com.agropredict.infrastructure.persistence.database.SqliteRow;

public final class UserPersistenceVisitor implements
        IUserIdentityConsumer, ICredentialConsumer, IUsernameConsumer, IPhoneConsumer, IOccupationConsumer {
    private final SqliteRow row;

    public UserPersistenceVisitor(SqliteRow row) {
        this.row = row;
    }

    @Override
    public void describe(String identifier, String fullName) {
        row.record("id", identifier);
        row.record("full_name", fullName);
    }

    @Override
    public void authenticate(String email, String password) {
        row.record("email", email);
        row.record("password_hash", password);
    }

    @Override
    public void enroll(String username) {
        row.record("username", username);
    }

    @Override
    public void contact(String number) {
        row.record("phone_number", number);
    }

    @Override
    public void classify(String identifier) {
        row.record("occupation_id", identifier);
    }
}