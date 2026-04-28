package com.agropredict.domain.input_validation;

public final class PasswordConfirmation {
    public boolean confirms(String password, String confirmation) {
        return password != null && password.equals(confirmation);
    }
}