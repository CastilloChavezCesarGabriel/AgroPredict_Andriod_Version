package com.agropredict.domain.visitor;

import com.agropredict.domain.value.user.UserData;
import com.agropredict.domain.value.user.UserIdentity;

public interface IUserVisitor {
    void visit(UserIdentity identity, UserData data);
}
