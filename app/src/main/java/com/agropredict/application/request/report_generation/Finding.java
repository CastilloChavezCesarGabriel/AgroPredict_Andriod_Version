package com.agropredict.application.request.report_generation;

import com.agropredict.domain.IIdentifierConsumer;
import com.agropredict.domain.visitor.report.IReportVisitor;

public final class Finding {
    private final String diagnosticIdentifier;
    private final String cropIdentifier;

    public Finding(String diagnosticIdentifier, String cropIdentifier) {
        this.diagnosticIdentifier = diagnosticIdentifier;
        this.cropIdentifier = cropIdentifier;
    }

    public void accept(IReportVisitor visitor) {
        visitor.visitContext(diagnosticIdentifier, cropIdentifier);
    }

    public void identify(IIdentifierConsumer consumer) {
        consumer.accept(diagnosticIdentifier);
    }
}
