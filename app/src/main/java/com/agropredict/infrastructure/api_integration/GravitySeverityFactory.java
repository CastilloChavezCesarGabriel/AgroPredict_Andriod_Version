package com.agropredict.infrastructure.api_integration;

import com.agropredict.domain.diagnostic.ISeverityFactory;
import com.agropredict.domain.diagnostic.Severity;
import com.agropredict.domain.diagnostic.SeverityClassifier;
import java.util.List;
import java.util.Objects;

public final class GravitySeverityFactory implements ISeverityFactory {
    private final List<SeverityClassifier> classifiers;
    private final Severity fallback;

    public GravitySeverityFactory(List<SeverityClassifier> classifiers, Severity fallback) {
        this.classifiers = List.copyOf(Objects.requireNonNull(classifiers, "gravity severity factory requires classifiers"));
        this.fallback = Objects.requireNonNull(fallback, "gravity severity factory requires a fallback severity");
    }

    @Override
    public Severity classify(String gravity) {
        for (SeverityClassifier classifier : classifiers) {
            Severity match = classifier.classify(gravity);
            if (match != null) return match;
        }
        return fallback;
    }
}