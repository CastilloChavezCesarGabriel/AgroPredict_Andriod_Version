package com.agropredict.infrastructure.report_export;

import com.agropredict.application.service.IReportService;
import com.agropredict.application.service.IReportServiceBuilder;
import com.agropredict.application.service.ReportFormat;
import java.io.File;

public final class PdfReportServiceBuilder implements IReportServiceBuilder {
    @Override
    public IReportService build(ReportFormat format, String directoryPath) {
        return format == ReportFormat.PDF ? new PdfReportService(new File(directoryPath)) : null;
    }
}
