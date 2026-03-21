package com.agropredict.application.visitor;

public interface ISessionResultVisitor {
    void visit(boolean active, String userIdentifier);
}