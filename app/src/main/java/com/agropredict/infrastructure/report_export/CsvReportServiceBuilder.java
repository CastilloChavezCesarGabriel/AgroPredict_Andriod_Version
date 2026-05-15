package com.agropredict.infrastructure.report_export;

import com.agropredict.application.service.IReportService;
import com.agropredict.application.service.IReportServiceBuilder;
import com.agropredict.application.service.ReportFormat;
import java.io.File;

public final class CsvReportServiceBuilder implements IReportServiceBuilder {
    @Override
    public IReportService build(ReportFormat format, String directoryPath) {
        return format == ReportFormat.CSV ? new CsvReportService(new File(directoryPath)) : null;
    }
}
