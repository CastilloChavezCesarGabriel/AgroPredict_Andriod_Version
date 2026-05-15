package com.agropredict.domain.input_validation.requirement;

public abstract class CharacterRequirement implements ICharacterRequirement {
    @Override
    public final boolean accepts(String password) {
        for (char character : password.toCharArray()) {
            if (matches(character)) {
                return true;
            }
        }
        return false;
    }

    protected abstract boolean matches(char character);
}