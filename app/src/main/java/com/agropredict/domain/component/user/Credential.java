package com.agropredict.domain.component.user;

import com.agropredict.domain.visitor.user.ICredentialsVisitor;

public final class Credential {
    private final String email;
    private final String passwordHash;

    public Credential(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public void accept(ICredentialsVisitor visitor) {
        visitor.visit(email, passwordHash);
    }

    public boolean matches(String candidateHash) {
        return passwordHash.equals(candidateHash);
    }
}