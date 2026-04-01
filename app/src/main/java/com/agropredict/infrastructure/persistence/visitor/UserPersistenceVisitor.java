package com.agropredict.infrastructure.persistence.visitor;

import com.agropredict.domain.visitor.user.IUserVisitor;
import com.agropredict.infrastructure.persistence.database.IRow;

public final class UserPersistenceVisitor implements IUserVisitor {
    private final IRow row;

    public UserPersistenceVisitor(IRow row) {
        this.row = row;
    }

    @Override
    public void visitIdentity(String identifier, String fullName) {
        row.record("id", identifier);
        row.record("full_name", fullName);
    }

    @Override
    public void visitCredential(String email, String passwordHash) {
        row.record("email", email);
        row.record("password_hash", passwordHash);
    }

    @Override
    public void visitUsername(String username) {
        row.record("username", username);
    }

    @Override
    public void visitPhone(String phoneNumber) {
        row.record("phone_number", phoneNumber);
    }

    @Override
    public void visitOccupation(String occupationIdentifier) {
        row.record("occupation_id", occupationIdentifier);
    }
}