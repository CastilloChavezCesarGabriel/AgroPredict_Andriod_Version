package com.agropredict.domain.visitor.user;

public interface ICredentialsVisitor {
    void visit(String email, String passwordHash);
}