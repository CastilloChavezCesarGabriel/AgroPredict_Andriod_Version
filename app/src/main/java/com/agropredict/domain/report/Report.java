package com.agropredict.domain.report;

import com.agropredict.domain.identifier.IIdentifierConsumer;
import com.agropredict.domain.guard.ArgumentPrecondition;

public final class Report {
    private final String identifier;
    private final String format;

    public Report(String identifier, String format) {
        this.identifier = ArgumentPrecondition.validate(identifier, "report identifier");
        this.format = ArgumentPrecondition.validate(format, "report format");
    }

    public void describe(IReportIdentityConsumer consumer) {
        consumer.describe(identifier, format);
    }

    public void identify(IIdentifierConsumer consumer) {
        consumer.identify(identifier);
    }
}
