package com.agropredict.domain.component.user;

import com.agropredict.domain.visitor.user.IUserContactVisitor;

public final class UserContact {
    private final String username;
    private final String phoneNumber;

    public UserContact(String username, String phoneNumber) {
        this.username = username;
        this.phoneNumber = phoneNumber;
    }

    public void accept(IUserContactVisitor visitor) {
        visitor.visitContact(username, phoneNumber);
    }
}