package com.agropredict.visitor;

import com.agropredict.domain.visitor.session.ISessionVisitor;

public final class TestSessionVisitor implements ISessionVisitor {
    private String userIdentifier;
    private String occupation;

    @Override
    public void visit(String userIdentifier, String occupation) {
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