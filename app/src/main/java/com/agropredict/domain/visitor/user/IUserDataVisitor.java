package com.agropredict.domain.visitor.user;

import com.agropredict.domain.component.user.Credential;
import com.agropredict.domain.component.user.UserProfile;

public interface IUserDataVisitor {
    void visit(Credential credential, UserProfile profile);
}
