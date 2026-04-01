package com.agropredict.domain.visitor.session;

public interface ISessionVisitor {
    void visit(String userIdentifier, String occupation);
}