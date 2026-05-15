package com.agropredict.domain.input_validation.rule;

import com.agropredict.domain.input_validation.failure.IValidationFailure;
import java.util.regex.Pattern;

public final class RegexRule extends TextRule {
    private final Pattern pattern;

    public RegexRule(String pattern, IValidationFailure failure) {
        super(failure);
        this.pattern = Pattern.compile(pattern);
    }

    @Override
    protected boolean accepts(String text) {
        return text != null && pattern.matcher(text).matches();
    }
}
