package com.agropredict.domain.input_validation;

public final class SpecialCharacterRequirement extends CharacterRequirement {
    @Override
    protected boolean matches(char character) {
        return !Character.isLetterOrDigit(character);
    }
}