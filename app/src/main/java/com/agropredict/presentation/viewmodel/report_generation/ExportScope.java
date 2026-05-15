package com.agropredict.presentation.viewmodel.report_generation;

import com.agropredict.application.report_generation.request.Destination;
import com.agropredict.application.report_generation.request.ReportRequest;
import com.agropredict.application.service.IReportService;
import com.agropredict.application.service.IReportServiceCatalog;
import com.agropredict.application.service.ReportFormat;
import com.agropredict.domain.crop.Crop;
import com.agropredict.domain.diagnostic.Diagnostic;
import java.util.Objects;

public final class ExportScope {
    private final ReportingData data;
    private final IReportServiceCatalog reportCatalog;
    private final String userIdentifier;

    public ExportScope(ReportingData data, IReportServiceCatalog reportCatalog, String userIdentifier) {
        this.data = Objects.requireNonNull(data, "export scope requires reporting data");
        this.reportCatalog = Objects.requireNonNull(reportCatalog, "export scope requires a report catalog");
        this.userIdentifier = Objects.requireNonNull(userIdentifier, "export scope requires a user identifier");
    }

    public Crop find(String cropIdentifier) {
        return data.find(cropIdentifier);
    }

    public Diagnostic resolve(String cropIdentifier) {
        return data.resolve(userIdentifier, cropIdentifier);
    }

    public IReportService prepare(ReportFormat format) {
        return reportCatalog.select(format);
    }

    public void archive(ReportRequest request, String filePath) {
        data.store(request, new Destination(userIdentifier, filePath));
    }
}
