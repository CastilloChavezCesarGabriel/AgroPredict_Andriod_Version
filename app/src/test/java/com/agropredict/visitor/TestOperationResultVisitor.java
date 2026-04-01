package com.agropredict.visitor;

import com.agropredict.application.visitor.IOperationResultVisitor;

public final class TestOperationResultVisitor implements IOperationResultVisitor {
    private boolean completed;
    private String resultIdentifier;

    @Override
    public void visit(boolean completed, String resultIdentifier) {
        this.completed = completed;
        this.resultIdentifier = resultIdentifier;
    }

    public boolean isCompleted() {
        return completed;
    }

    public boolean isCompleted(String identifier) {
        return completed && identifier.equals(resultIdentifier);
    }

    public boolean isRejected(String reason) {
        return !completed && reason.equals(resultIdentifier);
    }
}