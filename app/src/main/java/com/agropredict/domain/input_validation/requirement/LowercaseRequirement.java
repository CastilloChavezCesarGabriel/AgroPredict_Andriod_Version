package com.agropredict.domain.input_validation.requirement;

public final class LowercaseRequirement extends CharacterRequirement {
    @Override
    protected boolean matches(char character) {
        return Character.isLowerCase(character);
    }
}