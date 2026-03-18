package com.agropredict.domain.value.user;

import com.agropredict.domain.visitor.IUserDataVisitor;

public final class UserData {
    private final Credentials credentials;
    private final UserProfile profile;

    public UserData(Credentials credentials, UserProfile profile) {
        this.credentials = credentials;
        this.profile = profile;
    }

    public void accept(IUserDataVisitor visitor) {
        visitor.visit(credentials, profile);
    }

    public boolean authenticate(String candidateHash) {
        return credentials.isHashMatched(candidateHash);
    }
}