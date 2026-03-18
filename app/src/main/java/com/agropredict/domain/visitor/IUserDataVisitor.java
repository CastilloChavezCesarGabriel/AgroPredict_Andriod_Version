package com.agropredict.domain.visitor;

import com.agropredict.domain.value.user.Credentials;
import com.agropredict.domain.value.user.UserProfile;

public interface IUserDataVisitor {

    void visit(Credentials credentials, UserProfile profile);
}
