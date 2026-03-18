package com.agropredict.application.result;

import com.agropredict.application.consumer.IRegistrationResultConsumer;

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

    public static RegistrationResult fail(String errorMessage) {
        return new RegistrationResult(false, errorMessage);
    }

    public void accept(IRegistrationResultConsumer visitor) {
        visitor.visit(completed, errorMessage);
    }
}