package com.agropredict.presentation.viewmodel.report_generation;

import com.agropredict.application.report_generation.request.Destination;
import com.agropredict.application.report_generation.request.ReportRequest;
import com.agropredict.application.service.IReportService;
import com.agropredict.application.service.IReportServiceCatalog;
import com.agropredict.application.service.ReportFormat;
import com.agropredict.domain.crop.Crop;
import com.agropredict.domain.diagnostic.Diagnostic;
import java.util.Objects;

public final class ReportingSource {
    private final ReportingData data;
    private final IReportServiceCatalog reportCatalog;

    public ReportingSource(ReportingData data, IReportServiceCatalog reportCatalog) {
        this.data = Objects.requireNonNull(data, "reporting source requires reporting data");
        this.reportCatalog = Objects.requireNonNull(reportCatalog, "reporting source requires a report catalog");
    }

    public Crop find(String cropIdentifier) {
        return data.find(cropIdentifier);
    }

    public Diagnostic resolve(String userIdentifier, String cropIdentifier) {
        return data.resolve(userIdentifier, cropIdentifier);
    }

    public IReportService prepare(ReportFormat format) {
        return reportCatalog.select(format);
    }

    public void archive(ReportRequest request, Destination destination) {
        data.store(request, destination);
    }
}
