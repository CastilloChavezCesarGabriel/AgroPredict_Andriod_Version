package com.agropredict.domain.report;

public interface IReportStorageConsumer {
    void store(String userIdentifier, String filePath);
}