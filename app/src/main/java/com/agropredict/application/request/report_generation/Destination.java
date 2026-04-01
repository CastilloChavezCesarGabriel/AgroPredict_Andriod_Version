package com.agropredict.application.request.report_generation;

import com.agropredict.domain.visitor.report.IReportVisitor;

public final class Destination {
    private final String userIdentifier;
    private final String filePath;

    public Destination(String userIdentifier, String filePath) {
        this.userIdentifier = userIdentifier;
        this.filePath = filePath;
    }

    public void accept(IReportVisitor visitor) {
        visitor.visitStorage(userIdentifier, filePath);
    }
}