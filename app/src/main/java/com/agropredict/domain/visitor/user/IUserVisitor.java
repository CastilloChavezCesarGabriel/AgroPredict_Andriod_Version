package com.agropredict.domain.visitor.user;

import com.agropredict.domain.component.user.UserData;
import com.agropredict.domain.component.user.UserIdentity;

public interface IUserVisitor {
    void visit(UserIdentity identity, UserData data);
}