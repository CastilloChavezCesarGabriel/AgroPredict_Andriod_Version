package com.agropredict.visitor;

import com.agropredict.application.visitor.IRegistrationResultVisitor;

public final class TestRegistrationResultVisitor implements IRegistrationResultVisitor {
    private boolean completed;
    private String errorMessage;

    @Override
    public void visit(boolean completed, String errorMessage) {
        this.completed = completed;
        this.errorMessage = errorMessage;
    }

    public boolean isCompleted() {
        return completed;
    }

    public boolean isRejected(String message) {
        return !completed && errorMessage != null && errorMessage.contains(message);
    }
}