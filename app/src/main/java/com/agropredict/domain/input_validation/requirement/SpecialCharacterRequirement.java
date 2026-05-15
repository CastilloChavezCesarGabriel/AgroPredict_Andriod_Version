package com.agropredict.domain.input_validation.requirement;

public final class SpecialCharacterRequirement extends CharacterRequirement {
    @Override
    protected boolean matches(char character) {
        return !Character.isLetterOrDigit(character);
    }
}