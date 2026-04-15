package com.agropredict.domain;

import com.agropredict.domain.visitor.session.ISessionVisitor;

public final class Session {
    private final String userIdentifier;
    private final Occupation occupation;

    public Session(String userIdentifier, String occupation) {
        this.userIdentifier = userIdentifier;
        this.occupation = Occupation.of(occupation);
    }

    public void accept(ISessionVisitor visitor) {
        occupation.accept(visitor, userIdentifier);
    }

    public void observe(IOccupationHandler handler) {
        if (isActive()) occupation.accept(handler);
    }

    public boolean isActive() {
        return userIdentifier != null && !userIdentifier.isEmpty();
    }
}