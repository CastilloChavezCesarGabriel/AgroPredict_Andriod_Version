package com.agropredict.visitor;

import com.agropredict.domain.authentication.ISessionConsumer;

public final class TestSessionVisitor implements ISessionConsumer {
    private String userIdentifier;
    private String occupation;

    @Override
    public void report(String userIdentifier, String occupation) {
        this.userIdentifier = userIdentifier;
        this.occupation = occupation;
    }

    public boolean isIdentified(String identifier) {
        return identifier.equals(userIdentifier);
    }

    public boolean isOccupied(String role) {
        return role.equals(occupation);
    }
}