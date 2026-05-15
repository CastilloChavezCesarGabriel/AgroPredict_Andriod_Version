package com.agropredict.domain.input_validation.requirement;

public final class UppercaseRequirement extends CharacterRequirement {
    @Override
    protected boolean matches(char character) {
        return Character.isUpperCase(character);
    }
}