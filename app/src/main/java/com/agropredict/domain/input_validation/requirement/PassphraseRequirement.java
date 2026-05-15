package com.agropredict.domain.input_validation.requirement;

import java.util.HashSet;
import java.util.Set;

public final class PassphraseRequirement {
    private static final int MINIMUM_DISTINCT_CHARACTERS = 5;

    public boolean accepts(String passphrase) {
        Set<Character> characters = new HashSet<>();
        for (char symbol : passphrase.toCharArray()) {
            characters.add(symbol);
            if (characters.size() >= MINIMUM_DISTINCT_CHARACTERS) {
                return true;
            }
        }
        return false;
    }
}