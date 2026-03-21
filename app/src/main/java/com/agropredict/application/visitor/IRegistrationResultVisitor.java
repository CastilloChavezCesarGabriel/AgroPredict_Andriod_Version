package com.agropredict.application.visitor;

public interface IRegistrationResultVisitor {
    void visit(boolean completed, String errorMessage);
}