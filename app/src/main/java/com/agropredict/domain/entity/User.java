package com.agropredict.domain.entity;

import com.agropredict.domain.value.user.UserData;
import com.agropredict.domain.value.user.UserIdentity;
import com.agropredict.domain.visitor.IUserVisitor;

public final class User {
    private final UserIdentity identity;
    private final UserData data;

    private User(UserIdentity identity, UserData data) {
        this.identity = identity;
        this.data = data;
    }

    public static User create(UserIdentity identity, UserData data) {
        return new User(identity, data);
    }

    public void accept(IUserVisitor visitor) {
        visitor.visit(identity, data);
    }

    public boolean authenticate(String candidateHash) {
        return data.authenticate(candidateHash);
    }
}