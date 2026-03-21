package com.agropredict.domain.component.user;

import com.agropredict.domain.visitor.user.IUserProfileVisitor;

public final class UserProfile {
    private final UserContact contact;
    private final String occupationIdentifier;

    public UserProfile(UserContact contact, String occupationIdentifier) {
        this.contact = contact;
        this.occupationIdentifier = occupationIdentifier;
    }

    public void accept(IUserProfileVisitor visitor) {
        visitor.visit(contact, occupationIdentifier);
    }
}