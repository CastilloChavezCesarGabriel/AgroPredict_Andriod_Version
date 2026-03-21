package com.agropredict.domain.component.user;

import com.agropredict.domain.visitor.user.IUserDataVisitor;

public final class UserData {
    private final Credential credential;
    private final UserProfile profile;

    public UserData(Credential credential, UserProfile profile) {
        this.credential = credential;
        this.profile = profile;
    }

    public void accept(IUserDataVisitor visitor) {
        visitor.visit(credential, profile);
    }

    public boolean authenticate(String candidateHash) {
        return credential.matches(candidateHash);
    }
}