package com.agropredict.domain.visitor;

public interface IReportStorageVisitor {
    void visitStorage(String userIdentifier, String filePath);
}