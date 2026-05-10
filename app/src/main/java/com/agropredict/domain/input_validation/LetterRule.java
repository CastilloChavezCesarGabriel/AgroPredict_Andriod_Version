package com.agropredict.domain.input_validation;

public final class LetterRule extends TextRule {
    public LetterRule(String reason) {
        super(reason);
    }

    @Override
    protected boolean accepts(String text) {
        if (text == null) return false;
        for (int index = 0; index < text.length(); index++) {
            if (Character.isLetter(text.charAt(index))) return true;
        }
        return false;
    }
}