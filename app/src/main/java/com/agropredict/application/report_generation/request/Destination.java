package com.agropredict.application.report_generation.request;

import com.agropredict.domain.guard.ArgumentPrecondition;
import com.agropredict.domain.report.IReportStorageConsumer;

public final class Destination {
    private final String userIdentifier;
    private final String filePath;

    public Destination(String userIdentifier, String filePath) {
        this.userIdentifier = ArgumentPrecondition.validate(userIdentifier, "destination user identifier");
        this.filePath = ArgumentPrecondition.validate(filePath, "destination file path");
    }

    public void store(IReportStorageConsumer consumer) {
        consumer.store(userIdentifier, filePath);
    }
}