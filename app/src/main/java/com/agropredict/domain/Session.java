package com.agropredict.domain;

public final class Session {
    private final String userIdentifier;
    private final String occupation;

    public Session(String userIdentifier, String occupation) {
        this.userIdentifier = userIdentifier;
        this.occupation = occupation;
    }

    public void accept(ISessionVisitor visitor) {
        visitor.visit(userIdentifier, occupation);
    }

    public boolean isActive() {
        return userIdentifier != null && !userIdentifier.isEmpty();
    }

    public boolean isAdvanced() {
        if (occupation == null) return false;
        return occupation.equals("Agronomist")
                || occupation.equals("Specialist")
                || occupation.equals("Researcher");
    }
}
