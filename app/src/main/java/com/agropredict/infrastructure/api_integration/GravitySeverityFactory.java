package com.agropredict.infrastructure.api_integration;

import com.agropredict.domain.diagnostic.ISeverityFactory;
import com.agropredict.domain.diagnostic.Severity;
import com.agropredict.domain.diagnostic.SeverityClassifier;
import java.util.List;

public final class GravitySeverityFactory implements ISeverityFactory {
    private static final List<SeverityClassifier> CLASSIFIERS = List.of(
            new SeverityClassifier(List.of("bajo", "low"),
                    new Severity("low", "Healthy", 0)),
            new SeverityClassifier(List.of("moderado", "moderate"),
                    new Severity("moderate", "Moderate issue", 1)),
            new SeverityClassifier(List.of("alto", "high", "critico", "critical"),
                    new Severity("high", "Severe issue", 2)));
    private static final Severity UNKNOWN = new Severity(null, "Analysis complete", 0);

    @Override
    public Severity classify(String gravity) {
        for (SeverityClassifier classifier : CLASSIFIERS) {
            Severity match = classifier.classify(gravity);
            if (match != null) return match;
        }
        return UNKNOWN;
    }
}
