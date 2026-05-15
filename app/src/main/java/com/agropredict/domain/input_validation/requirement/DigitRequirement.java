package com.agropredict.domain.input_validation.requirement;

public final class DigitRequirement extends CharacterRequirement {
    @Override
    protected boolean matches(char character) {
        return Character.isDigit(character);
    }
}