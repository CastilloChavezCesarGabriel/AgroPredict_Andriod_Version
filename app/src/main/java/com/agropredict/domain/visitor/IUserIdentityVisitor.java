package com.agropredict.domain.visitor;

public interface IUserIdentityVisitor {
    void visitIdentity(String identifier, String fullName);
}