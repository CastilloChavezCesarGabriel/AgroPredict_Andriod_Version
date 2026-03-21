package com.agropredict.application.visitor;

public interface IOperationResultVisitor {
    void visit(boolean completed, String resultIdentifier);
}