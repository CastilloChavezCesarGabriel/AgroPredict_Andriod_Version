package com.agropredict.domain.entity;

import com.agropredict.domain.IIdentifierConsumer;
import com.agropredict.domain.visitor.report.IReportVisitor;

public final class Report {
    private final String identifier;
    private final String format;

    public Report(String identifier, String format) {
        this.identifier = identifier;
        this.format = format;
    }

    public void accept(IReportVisitor visitor) {
        visitor.visitIdentity(identifier, format);
    }

    public void identify(IIdentifierConsumer consumer) {
        consumer.accept(identifier);
    }
}
