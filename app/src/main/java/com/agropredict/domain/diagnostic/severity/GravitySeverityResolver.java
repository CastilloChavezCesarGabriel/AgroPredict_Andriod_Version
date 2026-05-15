package com.agropredict.domain.diagnostic.severity;

import java.util.List;
import java.util.Objects;

public final class GravitySeverityResolver implements ISeverityResolver {
    private final List<SeverityClassifier> classifiers;
    private final ISeverity fallback;

    public GravitySeverityResolver(List<SeverityClassifier> classifiers, ISeverity fallback) {
        this.classifiers = List.copyOf(Objects.requireNonNull(classifiers,
                "gravity severity resolver requires classifiers"));
        this.fallback = Objects.requireNonNull(fallback,
                "gravity severity resolver requires a fallback severity");
    }

    @Override
    public ISeverity classify(String gravity) {
        for (SeverityClassifier classifier : classifiers) {
            ISeverity match = classifier.classify(gravity);
            if (match != null) return match;
        }
        return fallback;
    }
}
