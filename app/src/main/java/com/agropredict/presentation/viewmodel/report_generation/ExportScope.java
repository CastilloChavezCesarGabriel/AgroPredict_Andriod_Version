package com.agropredict.presentation.viewmodel.report_generation;

import com.agropredict.application.report_generation.request.Destination;
import com.agropredict.application.report_generation.request.ReportRequest;
import com.agropredict.application.service.IReportService;
import com.agropredict.application.service.ReportFormat;
import com.agropredict.domain.crop.Crop;
import com.agropredict.domain.diagnostic.Diagnostic;
import java.util.Objects;

public final class ExportScope {
    private final ReportingSource source;
    private final String userIdentifier;

    public ExportScope(ReportingSource source, String userIdentifier) {
        this.source = Objects.requireNonNull(source, "export scope requires a reporting source");
        this.userIdentifier = Objects.requireNonNull(userIdentifier, "export scope requires a user identifier");
    }

    public Crop find(String cropIdentifier) {
        return source.find(cropIdentifier);
    }

    public Diagnostic resolve(String cropIdentifier) {
        return source.resolve(userIdentifier, cropIdentifier);
    }

    public IReportService prepare(ReportFormat format) {
        return source.prepare(format);
    }

    public void archive(ReportRequest request, String filePath) {
        source.archive(request, new Destination(userIdentifier, filePath));
    }
}
