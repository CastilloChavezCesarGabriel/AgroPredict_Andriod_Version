package com.agropredict.domain.component.user;

import com.agropredict.domain.visitor.user.IUserIdentityVisitor;

public final class UserIdentity {
    private final String identifier;
    private final String fullName;

    public UserIdentity(String identifier, String fullName) {
        this.identifier = identifier;
        this.fullName = fullName;
    }

    public void accept(IUserIdentityVisitor visitor) {
        visitor.visitIdentity(identifier, fullName);
    }
}