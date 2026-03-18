package com.agropredict.domain.value.user;

import com.agropredict.domain.visitor.ICredentialsVisitor;

public final class Credentials {
    private final String email;
    private final String passwordHash;

    public Credentials(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public void accept(ICredentialsVisitor visitor) {
        visitor.visit(email, passwordHash);
    }

    public boolean isHashMatched(String candidateHash) {
        return passwordHash.equals(candidateHash);
    }
}