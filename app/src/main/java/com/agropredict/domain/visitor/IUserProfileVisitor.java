package com.agropredict.domain.visitor;

import com.agropredict.domain.value.user.UserContact;

public interface IUserProfileVisitor {

    void visit(UserContact contact, String occupationIdentifier);
}
