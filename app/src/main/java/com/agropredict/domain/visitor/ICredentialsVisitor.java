package com.agropredict.domain.visitor;

public interface ICredentialsVisitor {
    void visit(String email, String passwordHash);
}