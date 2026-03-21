package com.agropredict.domain.visitor.user;

import com.agropredict.domain.component.user.UserContact;

public interface IUserProfileVisitor {
    void visit(UserContact contact, String occupationIdentifier);
}