package com.agropredict.infrastructure.report_export;

import com.agropredict.application.service.IReportService;
import com.agropredict.application.service.IReportServiceCatalog;
import com.agropredict.application.service.ReportFormat;
import java.util.Map;
import java.util.Objects;

public final class AndroidReportServiceCatalog implements IReportServiceCatalog {
    private final Map<ReportFormat, IReportService> services;

    public AndroidReportServiceCatalog(Map<ReportFormat, IReportService> services) {
        this.services = Map.copyOf(Objects.requireNonNull(services,
                "android report service catalog requires services"));
    }

    @Override
    public IReportService select(ReportFormat format) {
        IReportService service = services.get(format);
        if (service == null) {
            throw new IllegalStateException("no report service registered for format " + format);
        }
        return service;
    }
}
