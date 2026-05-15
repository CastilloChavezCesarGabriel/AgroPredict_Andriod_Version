package com.agropredict.infrastructure.factory;

import com.agropredict.application.factory.IReportingFactory;
import com.agropredict.application.repository.IReportRepository;
import java.util.Objects;

public final class AndroidReportingFactory implements IReportingFactory {
    private final ReportPersistence reportPersistence;

    public AndroidReportingFactory(ReportPersistence reportPersistence) {
        this.reportPersistence = Objects.requireNonNull(reportPersistence,
                "android reporting factory requires a report persistence");
    }

    @Override
    public IReportRepository createReportRepository() {
        return reportPersistence.createReportRepository();
    }
}