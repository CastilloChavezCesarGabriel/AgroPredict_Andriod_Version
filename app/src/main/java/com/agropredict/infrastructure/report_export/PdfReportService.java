package com.agropredict.infrastructure.report_export;

import com.agropredict.application.service.IReportWriter;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class PdfReportService extends ReportService {
    private static final String DISPLAY_FORMAT = "dd/MM/yyyy HH:mm";

    public PdfReportService(File outputDirectory) {
        super(outputDirectory);
    }

    @Override
    protected IReportWriter prepare(String timestamp) throws IOException {
        File file = new File(outputDirectory, "report_" + timestamp + ".pdf");
        PdfReport report = new PdfReport();
        report.open(file);
        return report;
    }

    @Override
    protected File complete(IReportWriter writer, String timestamp) {
        PdfReport report = (PdfReport) writer;
        String displayDate = new SimpleDateFormat(DISPLAY_FORMAT, Locale.getDefault()).format(new Date());
        report.close(displayDate);
        return new File(outputDirectory, "report_" + timestamp + ".pdf");
    }
}