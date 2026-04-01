package com.agropredict.infrastructure.report_export;

import com.agropredict.application.service.IReportWriter;
import java.io.File;
import java.io.IOException;

public final class CsvReportService extends ReportService {
    public CsvReportService(File outputDirectory) {
        super(outputDirectory);
    }

    @Override
    protected IReportWriter prepare(String timestamp) {
        return new CsvReport();
    }

    @Override
    protected File finalize(IReportWriter writer, String timestamp) throws IOException {
        CsvReport report = (CsvReport) writer;
        report.write("date", timestamp);
        File file = new File(outputDirectory, "report_" + timestamp + ".csv");
        report.export(file);
        return file;
    }
}