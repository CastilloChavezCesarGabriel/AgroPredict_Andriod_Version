package com.agropredict.application.operation_result;

import com.agropredict.application.visitor.IRegistrationResultVisitor;

public final class RegistrationResult {
    private final boolean completed;
    private final String errorMessage;

    private RegistrationResult(boolean completed, String errorMessage) {
        this.completed = completed;
        this.errorMessage = errorMessage;
    }

    public static RegistrationResult succeed() {
        return new RegistrationResult(true, null);
    }

    public static RegistrationResult fail(String message) {
        return new RegistrationResult(false, message);
    }

    public void accept(IRegistrationResultVisitor visitor) {
        visitor.visit(completed, errorMessage);
    }
}