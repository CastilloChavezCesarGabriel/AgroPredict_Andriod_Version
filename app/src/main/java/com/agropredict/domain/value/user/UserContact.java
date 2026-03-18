package com.agropredict.domain.value.user;

import com.agropredict.domain.visitor.IUserContactVisitor;

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