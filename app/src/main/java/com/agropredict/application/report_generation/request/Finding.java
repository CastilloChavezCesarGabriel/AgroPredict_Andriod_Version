package com.agropredict.application.report_generation.request;

import com.agropredict.domain.guard.ArgumentPrecondition;
import com.agropredict.domain.identifier.IIdentifierConsumer;
import com.agropredict.domain.report.IReportContextConsumer;

public final class Finding {
    private final String diagnosticIdentifier;
    private final String cropIdentifier;

    public Finding(String diagnosticIdentifier, String cropIdentifier) {
        this.diagnosticIdentifier = ArgumentPrecondition.validate(diagnosticIdentifier, "finding diagnostic identifier");
        this.cropIdentifier = ArgumentPrecondition.validate(cropIdentifier, "finding crop identifier");
    }

    public void link(IReportContextConsumer consumer) {
        consumer.link(diagnosticIdentifier, cropIdentifier);
    }

    public void identify(IIdentifierConsumer consumer) {
        consumer.identify(diagnosticIdentifier);
    }

    public void pair(IReportLinkConsumer consumer, String reportIdentifier) {
        consumer.pair(reportIdentifier, diagnosticIdentifier);
    }
}