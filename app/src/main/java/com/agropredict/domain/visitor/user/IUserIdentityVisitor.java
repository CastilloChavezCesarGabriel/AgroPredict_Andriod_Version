package com.agropredict.domain.visitor.user;

public interface IUserIdentityVisitor {
    void visitIdentity(String identifier, String fullName);
}