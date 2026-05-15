package com.agropredict.domain.diagnostic.severity;

import java.util.List;
import java.util.Objects;

public final class SeverityClassifier {
    private final List<String> keywords;
    private final ISeverity severity;

    public SeverityClassifier(List<String> keywords, ISeverity severity) {
        this.keywords = List.copyOf(Objects.requireNonNull(keywords, "severity classifier requires keywords"));
        this.severity = Objects.requireNonNull(severity, "severity classifier requires a severity");
    }

    public ISeverity classify(String gravity) {
        if (gravity == null) return null;
        String normalized = gravity.toLowerCase();
        for (String keyword : keywords) {
            if (normalized.contains(keyword)) {
                return severity;
            }
        }
        return null;
    }
}