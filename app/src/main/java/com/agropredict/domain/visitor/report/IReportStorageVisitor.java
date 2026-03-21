package com.agropredict.domain.visitor.report;

public interface IReportStorageVisitor {
    void visitStorage(String userIdentifier, String filePath);
}