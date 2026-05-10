package com.agropredict.domain.input_validation;

import java.util.regex.Pattern;

public final class RegexRule extends TextRule {
    private final Pattern pattern;

    public RegexRule(String pattern, String reason) {
        super(reason);
        this.pattern = Pattern.compile(pattern);
    }

    @Override
    protected boolean accepts(String text) {
        return text != null && pattern.matcher(text).matches();
    }
}
