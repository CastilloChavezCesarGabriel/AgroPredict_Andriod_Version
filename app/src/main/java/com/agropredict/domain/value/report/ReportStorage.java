package com.agropredict.domain.value.report;

import com.agropredict.domain.visitor.IReportStorageVisitor;

public final class ReportStorage {
    private final String userIdentifier;
    private final String filePath;

    public ReportStorage(String userIdentifier, String filePath) {
        this.userIdentifier = userIdentifier;
        this.filePath = filePath;
    }

    public void accept(IReportStorageVisitor visitor) {
        visitor.visitStorage(userIdentifier, filePath);
    }
}